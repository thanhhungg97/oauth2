package core.error

abstract class AppError(val message: String)

case class DataValidationError(override val message: String) extends AppError(message)
case class DatabaseAccessError(override val message: String) extends AppError(message)
