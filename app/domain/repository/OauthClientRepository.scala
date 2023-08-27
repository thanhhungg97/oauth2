package domain.repository

import domain.domain.{OauthClient, OauthId}
import domain.error.AppError
import zio._

trait OauthClientRepository {
  def save(oauthClient: OauthClient): IO[OauthClientRepositoryError, Int]

  def get(id: OauthId): IO[OauthClientRepositoryError, Option[OauthClient]]

  def update(oauthClient: OauthClient): IO[OauthClientRepositoryError, Unit]

}

sealed trait OauthClientRepositoryError extends Throwable

object OauthClientRepositoryError {
  final case class ServerError(cause: Throwable)
      extends Throwable(cause)
      with OauthClientRepositoryError
      with AppError.InternalError
}
