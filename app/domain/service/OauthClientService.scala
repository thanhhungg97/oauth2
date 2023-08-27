package domain.service

import domain.domain.{OauthClient, OauthId}
import domain.repository.{OauthClientRepository, OauthClientRepositoryError}
import libs.Telemetry
import zio._
import zio.logging.{LogLevel, Logger}

import java.util.UUID

trait OauthClientService {
  def registerOauthClient(
    redirectUri: String,
    scope: String
  ): IO[OauthClientServiceError, OauthId]

  def getOauthClientConfig(oauthId: OauthId): IO[OauthClientServiceError, Option[OauthClient]]

}

sealed trait OauthClientServiceError extends Throwable
object OauthClientServiceError {
  final case class RepositoryError(cause: domain.repository.OauthClientRepositoryError)
      extends Throwable(cause)
      with OauthClientServiceError
}

object OauthClientService {
  def registerOauthClient(
    redirectUri: String,
    scope: String
  ): ZIO[Has[OauthClientService], OauthClientServiceError, OauthId] =
    ZIO.serviceWith[OauthClientService](
      _.registerOauthClient(redirectUri, scope)
    )

  def getOauthClientConfig(
    oauthId: OauthId
  ): ZIO[Has[OauthClientService], OauthClientServiceError, Option[OauthClient]] =
    ZIO.serviceWith[OauthClientService](_.getOauthClientConfig(oauthId))
}

case class OauthClientServiceImpl(
  oauthClientRepository: OauthClientRepository,
  oauthSecretGenerator: OauthSecretGenerator,
  logging: Logger[String]
) extends OauthClientService
    with Telemetry {
  def registerOauthClient(
    redirectUri: String,
    scope: String
  ): IO[OauthClientServiceError, OauthId] =
    for {
      secret  <- oauthSecretGenerator.generate()
      oauthId <- ZIO.succeed(OauthId(UUID.randomUUID()))
      _ <- oauthClientRepository
             .save(
               OauthClient(oauthId, secret, redirectUri, scope)
             )
             .mapError(handleRepositoryError)
    } yield oauthId

  def getOauthClientConfig(
    oauthId: OauthId
  ): IO[OauthClientServiceError, Option[OauthClient]] = log("UserManagementService.create", LogLevel.Info) {
    oauthClientRepository.get(oauthId).mapError(handleRepositoryError)
  }

  private def handleRepositoryError(error: OauthClientRepositoryError): OauthClientServiceError =
    OauthClientServiceError.RepositoryError(error)
}
