package error

import common.AppError
sealed trait UserManagementServiceError extends Throwable

object UserManagementServiceError {
  final case class PasswordServiceError(cause: PasswordError) extends Throwable(cause) with UserManagementServiceError

  final case class RepositoryError(cause: UserRepositoryError) extends Throwable(cause) with UserManagementServiceError

  final case class DuplicateUserName(name: String)
      extends Throwable(s"Duplicate User Name: $name")
      with UserManagementServiceError
      with AppError.DomainError

}
