package domain.service

import domain.domain.Password
import domain.error.AppError
import domain.service.PasswordError.InvalidPasswordPolicy
import zio.{Function2ToLayerSyntax, IO}

trait PasswordService {
  def encode(maybePassword: String): IO[PasswordError, Password]
}

sealed trait PasswordError extends Throwable

object PasswordError {
  final case class InvalidPasswordPolicy(cause: PasswordPolicyError)
      extends Throwable(cause)
      with PasswordError
      with AppError.ValidationError

}

object PasswordService {
  val layer = (PasswordServiceImpl.apply _).toLayer[PasswordService]
}

case class PasswordServiceImpl(passwordPolicy: PasswordPolicy, encoder: Encoder) extends PasswordService {

  override def encode(maybePassword: String): IO[PasswordError, Password] =
    for {
      _               <- passwordPolicy.validate(maybePassword).mapError(handlePasswordPolicyError)
      encodedPassword <- encoder.encode(maybePassword)
    } yield Password(encodedPassword)
  private[this] def handlePasswordPolicyError(error: PasswordPolicyError): PasswordError = InvalidPasswordPolicy(error)

}
