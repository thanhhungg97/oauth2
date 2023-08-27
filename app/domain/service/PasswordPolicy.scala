package domain.service

import domain.service.PasswordPolicyError.InvalidPassword
import zio.{Function0ToLayerSyntax, IO, ZIO}

trait PasswordPolicy {
  def validate(maybePassword: String): IO[PasswordPolicyError, Unit]

}

object PasswordPolicy {
  val layer = (SimplePasswordPolicy.apply _).toLayer[PasswordPolicy]
}

sealed trait PasswordPolicyError extends Throwable

object PasswordPolicyError {
  case class InvalidPassword(message: String) extends Throwable(message) with PasswordPolicyError
}

case class SimplePasswordPolicy() extends PasswordPolicy {
  override def validate(maybePassword: String): IO[PasswordPolicyError, Unit] =
    for {
      _ <- ZIO.cond(maybePassword.nonEmpty, (), InvalidPassword("Password is empty"))
      _ <- ZIO.cond(maybePassword.length >= 6, (), InvalidPassword("Password length is not enough"))
    } yield ()

}
