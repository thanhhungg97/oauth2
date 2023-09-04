package controllers.format

import play.api.libs.json.{Json, OWrites}
import domain._
object OauthClientFormat {
  implicit val oauthClientFormat: OWrites[OauthClient] = oauthClient =>
    Json.obj(
      "id"           -> oauthClient.id.uuid,
      "redirect_uri" -> oauthClient.redirectUri,
      "scopes"       -> oauthClient.scopes
    )
}
