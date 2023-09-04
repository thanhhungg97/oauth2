package layer

import repository.{OauthClientMysqlRepository, OauthClientRepository}
import zio._

object OauthClientRepositoryLayer {
  private val pure = OauthClientMysqlRepository.toLayer[OauthClientRepository]

  val layer: ULayer[Has[OauthClientRepository]] = zio.blocking.Blocking.live >>> pure
}
