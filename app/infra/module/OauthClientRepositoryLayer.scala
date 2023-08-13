package infra.module

import core.repository.OauthClientRepository
import infra.repository.OauthClientMysqlRepository
import zio._

object OauthClientRepositoryLayer {
  val layer: ULayer[Has[OauthClientRepository]] = OauthClientMysqlRepository.toLayer[OauthClientRepository]
}
