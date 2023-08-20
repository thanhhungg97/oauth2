package domain.command.request

case class CreateOauthClientRequest(redirectURI: String, scope: String)
