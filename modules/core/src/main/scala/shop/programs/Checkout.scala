package shop.programs

import cats.MonadThrow
import cats.syntax.all._
import org.typelevel.log4cats.Logger
import retry.RetryPolicies.{ exponentialBackoff, limitRetries }
import retry._
import shop.domain.auth.UserId
import shop.domain.cart._
import shop.domain.checkout._
import shop.domain.order._
import shop.domain.payment._
import shop.effects.Background
import shop.http.clients.PaymentClient
import shop.retries.{ Retriable, Retry }
import shop.services._
import squants.market.Money

import scala.concurrent.duration._

final case class Checkout[F[_]: MonadThrow[F]](
    payments: PaymentClient[F],
    cart: ShoppingCart[F],
    orders: Orders[F],
    policy: RetryPolicy[F]
) {

  val retryPolicy: RetryPolicy[F] = limitRetries[F](3) |+| exponentialBackoff[F](10.milliseconds)

  def processPayment(in: Payment): F[PaymentId] =
    Retry[F]
      .retry(policy, Retriable.Payments)(payments.process(in))
      .addaptError {
        case e => PaymentError(Option(e.getMessage).getOrElse("Unknown error"))
      }

  def process(userId: UserId, card: Card): F[OrderId] = {
    cart
      .get(userId)
      .ensure(EmptyCartError)(_.items.nonEmpty)
      .flatMap {
        case CartTotal(items, total) =>
          for {
            pId <- payments.process(Payment(userId, total, card))
            oid <- orders.create(userId, pId, items, total)
            _   <- cart.delete(userId).attempt.void
          } yield oid
      }
  }

  def createOrder(userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money) = {
    val action = Retry[F]
      .retry(policy, Retriable.Order)(orders.create(userId, paymentId, items, total))
      .adaptError {
        case e => OrderError(e.getMessage)
      }

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.onError {
        case _ =>
          Logger[F].error(s"Failed to create order for ${paymentId.show}")
          *>
          Background[F].schedule(bgAction(fa), 1.hour)
      }

    bgAction(action)
  }
}
