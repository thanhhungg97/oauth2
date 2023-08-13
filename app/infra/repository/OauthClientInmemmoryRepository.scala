package infra.repository

import core.domain.{OauthClient, OauthId}
import core.error.DatabaseAccessError
import core.repository.OauthClientRepository
import zio._

import java.util.UUID
import scala.collection.immutable.TreeMap


case class OauthClientInmemmoryRepository() extends OauthClientRepository {
  private val datas = TreeMap.empty[UUID, OauthClient]

  override def save(oauthClient: OauthClient): IO[DatabaseAccessError, Int] = for {
    _ <- ZIO.succeed(datas + (oauthClient.id.uuid -> oauthClient))
  } yield 1

  override def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]] = ZIO.succeed(datas.get(id.uuid))

  override def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit] = ZIO.succeed(datas + (oauthClient.id.uuid -> oauthClient))
}
