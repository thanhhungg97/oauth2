package error

import common.AppError

sealed trait PasswordError extends Throwable

object PasswordError {
  final case class InvalidPasswordPolicy(cause: PasswordPolicyError)
      extends Throwable(cause)
      with PasswordError
      with AppError.ValidationError

}
