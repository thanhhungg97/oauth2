package command

case class CreateUserRequest(username: String, password: String, email: Option[String], phoneNumber: Option[String])
