package core.domain

import java.util.UUID


case class OauthId(uuid: UUID)

case class OauthSecret(secret: String) {
  override def toString: String = s"Reacted<${secret.substring(4)}>"
}

case class OauthClient(id: OauthId, clientSecret: OauthSecret, redirectUri: String, scope: String)

