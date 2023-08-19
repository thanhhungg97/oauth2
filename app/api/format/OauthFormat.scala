package api.format

import core.command.request.CreateOauthClientRequest
import core.domain.OauthId
import play.api.libs.json.{Json, OFormat, OWrites}

object OauthFormat {
  implicit val createOauthRequestFormat: OFormat[CreateOauthClientRequest] =
    Json.format[CreateOauthClientRequest]
  implicit val createOauthClientResponseFormat: OWrites[OauthId] = oauthId => Json.obj("id" -> oauthId.uuid)

}
