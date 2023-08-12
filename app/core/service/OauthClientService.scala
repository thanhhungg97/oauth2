package core.service

import core.domain.{OauthClient, OauthId}
import core.error.AppError
import core.repository.OauthClientRepository
import zio._

import java.util.UUID

trait OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): IO[AppError, OauthId]

}

object OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): ZIO[Has[OauthClientService], AppError, OauthId] = ZIO.serviceWith[OauthClientService](_.registerOauthClient(redirectUri, scope))
}


case class OauthClientServiceImpl(oauthClientRepository: OauthClientRepository, oauthSecretGenerator: OauthSecretGenerator) extends OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): IO[AppError, OauthId] = {
    for {
      secret <- oauthSecretGenerator.generate()
      oauthId <- ZIO.succeed(OauthId(UUID.randomUUID()))
      _ <- oauthClientRepository.save(OauthClient(oauthId, secret, redirectUri, scope))
    } yield oauthId
  }

}

