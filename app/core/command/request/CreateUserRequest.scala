package core.command.request

case class CreateUserRequest(username: String, password: String, email: Option[String], phoneNumber: Option[Int])
