package infra.module

import core.repository.OauthClientRepository
import infra.repository.OauthClientInmemmoryRepository
import zio._

object OauthClientRepositoryLayer {
  val layer: ULayer[Has[OauthClientRepository]] = OauthClientInmemmoryRepository.toLayer[OauthClientRepository]
}
