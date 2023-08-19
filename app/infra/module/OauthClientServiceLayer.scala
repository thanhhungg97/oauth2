package infra.module

import core.repository.OauthClientRepository
import core.service.{OauthClientService, OauthClientServiceImpl, OauthSecretGenerator, OauthSecretGeneratorImp}
import zio.random.Random
import zio.{Function2ToLayerSyntax, Has, ULayer, URLayer}

object OauthClientServiceLayer {
  val layer: URLayer[Has[OauthClientRepository] with Has[OauthSecretGenerator], Has[OauthClientService]] =
    (OauthClientServiceImpl.apply _).toLayer[OauthClientService]
}
object OauthSecretGeneratorLayer {
  val layer: URLayer[Has[Random.Service], Has[OauthSecretGenerator]] =
    (OauthSecretGeneratorImp.apply _).toLayer[OauthSecretGenerator]
  val live: ULayer[Has[OauthSecretGenerator]] =
    zio.random.Random.live >>> layer
}
