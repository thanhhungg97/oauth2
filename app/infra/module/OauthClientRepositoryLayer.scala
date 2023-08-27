package infra.module

import domain.repository.OauthClientRepository
import infra.repository.OauthClientMysqlRepository
import zio._

object OauthClientRepositoryLayer {
  private val pure = OauthClientMysqlRepository.toLayer[OauthClientRepository]

  val layer: ULayer[Has[OauthClientRepository]] = zio.blocking.Blocking.live >>> pure
}
