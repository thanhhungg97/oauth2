package core.service

import core.domain.OauthSecret
import zio.random._
import zio.{Has, UIO, URLayer, ZIO, ZLayer}

trait OauthSecretGenerator {
  def generate(): UIO[OauthSecret]
}

object OauthSecretGenerator {
  val layer: URLayer[Has[Random.Service], Has[OauthSecretGenerator]] = (OauthSecretGeneratorImp.apply _).toLayer[OauthSecretGenerator]
  val live: ZLayer[Any, Nothing, Has[OauthSecretGenerator]] =  Random.live >>> layer
}


case class OauthSecretGeneratorImp(random: Random.Service) extends OauthSecretGenerator {
  override def generate(): ZIO[Any, Nothing, OauthSecret] = {
    for {
      maybeRandom <- random.nextString(1231)
    } yield OauthSecret(maybeRandom)
  }
}