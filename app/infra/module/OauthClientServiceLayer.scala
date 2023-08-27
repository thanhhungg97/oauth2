package infra.module

import domain.repository.OauthClientRepository
import domain.service.{OauthClientService, OauthClientServiceImpl, OauthSecretGenerator, OauthSecretGeneratorImp}
import zio.logging.Logger
import zio.random.Random
import zio.{Has, ULayer, URLayer}

object OauthClientServiceLayer {
  val layer: URLayer[Has[OauthClientRepository] with Has[OauthSecretGenerator] with Has[Logger[String]], Has[
    OauthClientService
  ]] =
    (OauthClientServiceImpl.apply _).toLayer[OauthClientService]
}
object OauthSecretGeneratorLayer {
  val layer: URLayer[Has[Random.Service], Has[OauthSecretGenerator]] =
    (OauthSecretGeneratorImp.apply _).toLayer[OauthSecretGenerator]
  val live: ULayer[Has[OauthSecretGenerator]] =
    zio.random.Random.live >>> layer
}
