package domain.service

import domain.error.{InvalidPassword, PasswordError}
import zio.{Function0ToLayerSyntax, IO, ZIO}

trait PasswordPolicy {
  def validate(maybePassword: String): IO[PasswordError, Unit]

}

object PasswordPolicy {
  val layer = (SimplePasswordPolicy.apply _).toLayer[PasswordPolicy]
}

case class SimplePasswordPolicy() extends PasswordPolicy {
  override def validate(maybePassword: String): IO[PasswordError, Unit] =
    for {
      _ <- ZIO.cond(maybePassword.nonEmpty, (), InvalidPassword("Password is empty"))
      _ <- ZIO.cond(maybePassword.length >= 6, (), InvalidPassword("Password length is not enough"))
    } yield ()

}
