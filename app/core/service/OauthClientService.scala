package core.service

import core.domain.{OauthClient, OauthId}
import core.error.AppError
import core.repository.OauthClientRepository
import zio._

import java.util.UUID

trait OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): IO[AppError, OauthId]

  def getOauthClientConfig(oauthId: OauthId): IO[AppError, Option[OauthClient]]

}

object OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): ZIO[Has[OauthClientService], AppError, OauthId] = ZIO.serviceWith[OauthClientService](_.registerOauthClient(redirectUri, scope))

  def getOauthClientConfig(oauthId: OauthId): ZIO[Has[OauthClientService], AppError, Option[OauthClient]] = ZIO.serviceWith[OauthClientService](_.getOauthClientConfig(oauthId))
}


case class OauthClientServiceImpl(oauthClientRepository: OauthClientRepository, oauthSecretGenerator: OauthSecretGenerator) extends OauthClientService {
  def registerOauthClient(redirectUri: String, scope: String): IO[AppError, OauthId] = {
    for {
      secret <- oauthSecretGenerator.generate()
      oauthId <- ZIO.succeed(OauthId(UUID.randomUUID()))
      _ <- oauthClientRepository.save(OauthClient(oauthId, secret, redirectUri, scope))
    } yield oauthId
  }

  def getOauthClientConfig(oauthId: OauthId): IO[AppError, Option[OauthClient]] = {
    for {
      maybeOauth <- oauthClientRepository.get(oauthId)
    } yield maybeOauth
  }
}

