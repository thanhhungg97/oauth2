package domain.repository

import domain.domain.{OauthClient, OauthId}
import domain.error.DatabaseAccessError
import zio._

trait OauthClientRepository {
  def save(oauthClient: OauthClient): IO[DatabaseAccessError, Int]

  def get(id: OauthId): IO[DatabaseAccessError, Option[OauthClient]]

  def update(oauthClient: OauthClient): IO[DatabaseAccessError, Unit]

}
