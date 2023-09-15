package repository

import domain.{OauthClient, OauthId}
import error.OauthClientRepositoryError
import zio._

trait OauthClientRepository {
  def save(oauthClient: OauthClient): IO[OauthClientRepositoryError, Int]

  def get(id: OauthId): IO[OauthClientRepositoryError, Option[OauthClient]]

  def update(oauthClient: OauthClient): IO[OauthClientRepositoryError, Unit]

}
