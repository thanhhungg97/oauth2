package controllers.format

import domain.command.request.CreateUserRequest
import domain.domain.User
import play.api.libs.json.{Json, OFormat, OWrites}

object UserFormat {
  implicit val userWriteFormat: OWrites[User] = user => {
    Json.obj(
      "id"    -> user.id,
      "email" -> user.email.map(_.value),
      "phone" -> user.phoneNumber.map(_.value)
    )
  }
  implicit val userCreateRequestFormat: OFormat[CreateUserRequest] = Json.format[CreateUserRequest]

}
