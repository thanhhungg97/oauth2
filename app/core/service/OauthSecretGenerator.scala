package core.service

import core.domain.OauthSecret
import zio.random._
import zio.{UIO, ZIO}

trait OauthSecretGenerator {
  def generate(): UIO[OauthSecret]
}



case class OauthSecretGeneratorImp(random: Random.Service) extends OauthSecretGenerator {
  override def generate(): ZIO[Any, Nothing, OauthSecret] = {
    for {
      maybeRandom <- random.nextString(1231)
    } yield OauthSecret(maybeRandom)
  }
}