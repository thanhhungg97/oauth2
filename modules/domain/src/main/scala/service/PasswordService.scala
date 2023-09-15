package service

import domain.Password
import error.PasswordError.InvalidPasswordPolicy
import error.{PasswordError, PasswordPolicyError}
import zio.macros.accessible
import zio.{Function2ToLayerSyntax, IO}

@accessible
trait PasswordService {
  def encode(maybePassword: String): IO[PasswordError, Password]
}

case class PasswordServiceImpl(passwordPolicy: PasswordPolicy, encoder: Encoder) extends PasswordService {

  override def encode(maybePassword: String): IO[PasswordError, Password] =
    for {
      _               <- passwordPolicy.validate(maybePassword).mapError(handlePasswordPolicyError)
      encodedPassword <- encoder.encode(maybePassword)
    } yield Password(encodedPassword)
  private[this] def handlePasswordPolicyError(error: PasswordPolicyError): PasswordError = InvalidPasswordPolicy(error)

}

object PasswordService {
  val layer = (PasswordServiceImpl.apply _).toLayer[PasswordService]
}
