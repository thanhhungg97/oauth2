package repository

import zio.IO
import domain._
import error.UserRepositoryError

trait UserRepository {
  def save(user: User): IO[UserRepositoryError, String]
  def get(id: String): IO[UserRepositoryError, Option[User]]
  def getByUsername(username: String): IO[UserRepositoryError, Option[User]]
}




