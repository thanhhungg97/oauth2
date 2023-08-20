package domain.repository

import domain.domain.User
import domain.error.DatabaseAccessError
import zio.IO

trait UserRepository {
  def save(user: User): IO[DatabaseAccessError, String]
  def get(id: String): IO[DatabaseAccessError, Option[User]]

  def getByUsername(username: String): IO[DatabaseAccessError, Option[User]]
}
