package shop.retries

sealed trait Retriable

object Retriable {
  case object Order    extends Retriable
  case object Payments extends Retriable
}
