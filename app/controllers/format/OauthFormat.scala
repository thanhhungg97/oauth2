package controllers.format

import domain.OauthId
import play.api.libs.json.{Json, OFormat, OWrites}
import command.CreateOauthClientRequest

object OauthFormat {
  implicit val createOauthRequestFormat: OFormat[CreateOauthClientRequest] =
    Json.format[CreateOauthClientRequest]
  implicit val createOauthClientResponseFormat: OWrites[OauthId] = oauthId => Json.obj("id" -> oauthId.uuid)

}
