package shop.domain

import io.estatico.newtype.macros.newtype
import shop.domain.cart.Quantity
import shop.domain.item.ItemId

import java.util.UUID

object order {

  @newtype case class OrderId(uuid: UUID)
  @newtype case class PaymentId(uuid: UUID)

  case class Order(id: OrderId, pId: PaymentId, items: Map[ItemId, Quantity])

}
