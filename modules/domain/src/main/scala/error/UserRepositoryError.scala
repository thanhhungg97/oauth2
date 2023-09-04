package error

trait UserRepositoryError extends Throwable

object UserRepositoryError {
  case class ServiceError(cause: Throwable) extends Throwable(cause) with UserRepositoryError

  case class ValidationError(cause: String) extends Throwable(cause) with UserRepositoryError

}