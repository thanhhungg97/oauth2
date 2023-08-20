package domain.error

abstract class AppError(val message: String)

case class DataValidationError(override val message: String) extends AppError(message)
case class DatabaseAccessError(override val message: String) extends AppError(message)

abstract class UserError(override val message: String) extends AppError(message)

case class InvalidEmail(email: String) extends UserError(s"Invalid email $email")

case class DuplicateUserName(userName: String) extends UserError(s"Duplicate username $userName")

abstract class PasswordError(override val message: String) extends UserError(message)




case class InvalidPassword(reason: String) extends PasswordError(s"Invalid password format $reason")
