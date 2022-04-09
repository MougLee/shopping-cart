package shop.domain

object checkout {

  case class Card(name: CardName, number: CardNumber, expiration: CardExpiration, ccv: CardCVV)

}
