package api.dto

case class CreateUserRequest(userName: String, password: String, email: Option[String], phoneNumber: Option[String])
