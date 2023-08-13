package core.domain

import java.util.UUID

case class OauthId(uuid: UUID)

case class OauthSecret(secret: String)
case class OauthClient(id: OauthId, clientSecret: OauthSecret, redirectUri: String, scopes: String)
