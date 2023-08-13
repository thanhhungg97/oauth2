package core.repository

import core.domain.{OauthClient, OauthId}
import core.error.DatabaseAccessError
import zio._

trait OauthClientRepository {
  def save(oauthClient: OauthClient): IO[DatabaseAccessError, Int]

  def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]]

  def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit]

}
