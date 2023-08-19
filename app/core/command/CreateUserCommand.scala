package core.command

case class CreateUserCommand(userName: String, password: String, email: Option[String], phoneNumber: Option[Int])
