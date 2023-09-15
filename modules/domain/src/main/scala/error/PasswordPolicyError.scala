package error

sealed trait PasswordPolicyError extends Throwable

object PasswordPolicyError {
  case class InvalidPassword(message: String) extends Throwable(message) with PasswordPolicyError
}
