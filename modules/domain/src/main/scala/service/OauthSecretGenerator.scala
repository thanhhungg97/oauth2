package service

import domain.OauthSecret
import zio.UIO
import zio.random._

trait OauthSecretGenerator {
  def generate(): UIO[OauthSecret]
}

case class OauthSecretGeneratorImp(random: Random.Service) extends OauthSecretGenerator {
  override def generate(): UIO[OauthSecret] =
    for {
      maybeRandom <- random.nextUUID
    } yield OauthSecret(maybeRandom.toString)
}
