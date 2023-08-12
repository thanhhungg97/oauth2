package core.repository

import core.domain.{OauthClient, OauthId}
import core.error.DatabaseAccessError
import zio._

import java.util.UUID
import scala.collection.immutable.TreeMap

trait OauthClientRepository {
  def save(oauthClient: OauthClient): IO[DatabaseAccessError, Unit]

  def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]]

  def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit]

}

object OauthClientRepository {
  val layer: ULayer[Has[OauthClientRepository]] = OauthClientInmemmoryRepository.toLayer[OauthClientRepository]
}


case class OauthClientInmemmoryRepository() extends OauthClientRepository {
  private val datas = TreeMap.empty[UUID, OauthClient]

  override def save(oauthClient: OauthClient): IO[DatabaseAccessError, Unit] = ZIO.succeed(datas + (oauthClient.id.uuid -> oauthClient))

  override def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]] = ZIO.succeed(datas.get(id.uuid))

  override def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit] = ZIO.succeed(datas + (oauthClient.id.uuid -> oauthClient))
}
