package shop.domain

import io.circe.Encoder
import io.estatico.newtype.macros.newtype
import monocle.Iso

object healthcheck {

  @newtype case class RedisStatus(value: Status)
  @newtype case class PostgresStatus(value: Status)
  @newtype case class AppStatus(redis: RedisStatus, postgres: PostgresStatus)

  sealed trait Status
  object Status {
    case object Okay        extends Status
    case object Unreachable extends Status

    val _Bool: Iso[Status, Boolean] = Iso[Status, Boolean] {
      case Okay        => true
      case Unreachable => false
    }(if (_) Okay else Unreachable)

    implicit val jsonEncoder: Encoder[Status] = Encoder.forProduct1("status")(_.toString)
  }
}
