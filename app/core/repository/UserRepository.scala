package core.repository

import core.domain.User
import core.error.DatabaseAccessError
import zio.IO

trait UserRepository {
  def save(user: User): IO[DatabaseAccessError, Int]
  def get(id: String): IO[DatabaseAccessError, Option[User]]

}
