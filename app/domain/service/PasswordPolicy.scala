package domain.service

import domain.error.PasswordPolicyError
import domain.error.PasswordPolicyError.InvalidPassword
import zio.macros.accessible
import zio.{Function0ToLayerSyntax, IO, ZIO}
@accessible
trait PasswordPolicy {
  def validate(maybePassword: String): IO[PasswordPolicyError, Unit]

}

case class SimplePasswordPolicy() extends PasswordPolicy {
  override def validate(maybePassword: String): IO[PasswordPolicyError, Unit] =
    for {
      _ <- ZIO.cond(maybePassword.nonEmpty, (), InvalidPassword("Password is empty"))
      _ <- ZIO.cond(maybePassword.length >= 6, (), InvalidPassword("Password length is not enough"))
    } yield ()

}

object PasswordPolicy {
  val layer = (SimplePasswordPolicy.apply _).toLayer[PasswordPolicy]
}
