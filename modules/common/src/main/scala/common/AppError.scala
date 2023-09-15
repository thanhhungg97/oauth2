package common

trait AppError extends Throwable
object AppError {
  trait ValidationError extends AppError
  trait DomainError     extends AppError
  trait InternalError   extends AppError
  trait ExternalError   extends AppError

}
