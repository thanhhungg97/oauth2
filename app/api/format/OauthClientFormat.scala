package api.format

import core.domain.OauthClient
import play.api.libs.json.{Json, OWrites}

object OauthClientFormat {
  implicit val oauthClientFormat: OWrites[OauthClient] = oauthClient =>
    Json.obj(
      "id"           -> oauthClient.id.uuid,
      "redirect_uri" -> oauthClient.redirectUri,
      "scopes"       -> oauthClient.scopes
    )
}
