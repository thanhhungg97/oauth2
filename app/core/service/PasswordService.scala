package core.service

import core.domain.Password
import core.error.PasswordError
import zio.{Function2ToLayerSyntax, IO}

trait PasswordService {
  def encode(maybePassword: String): IO[PasswordError, Password]
}

object PasswordService {
  val layer = (PasswordServiceImpl.apply _).toLayer[PasswordService]
}

case class PasswordServiceImpl(passwordPolicy: PasswordPolicy, encoder: Encoder) extends PasswordService {
  override def encode(maybePassword: String): IO[PasswordError, Password] =
    for {
      _               <- passwordPolicy.validate(maybePassword)
      encodedPassword <- encoder.encode(maybePassword)
    } yield Password(encodedPassword)

}
