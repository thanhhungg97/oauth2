package domain.repository

import domain.domain.User
import zio.IO

trait UserRepository {
  def save(user: User): IO[UserRepositoryError, String]
  def get(id: String): IO[UserRepositoryError, Option[User]]

  def getByUsername(username: String): IO[UserRepositoryError, Option[User]]
}

trait UserRepositoryError extends Throwable

object UserRepositoryError {
  case class ServiceError(cause: Throwable) extends Throwable(cause) with UserRepositoryError

  case class ValidationError(cause: String) extends Throwable(cause) with UserRepositoryError

}
