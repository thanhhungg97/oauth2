package infra.repository

import core.domain.{Email, Password, PhoneNumber, User}
import core.error.DatabaseAccessError
import core.repository.UserRepository
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.blocking.Blocking
import zio.{IO, ZIO}

case class UserMysqlRepository(blocking: Blocking.Service) extends UserRepository {
  override def save(user: User): IO[DatabaseAccessError, String] =
    blocking.effectBlocking {
      DB.autoCommit { implicit session =>
        val rowEffected =
          sql"""insert into user(`id`, `username`,  `password`, `email`, `phone_number`)
                       values
                       (${user.id}, ${user.username}, ${user.password.value},${
            user.email
              .map(_.value)
              .getOrElse("")
          },${user.phoneNumber.map(_.value).getOrElse("")})

            """
            .update()
            .apply()
        Either.cond(rowEffected == 1, user.id, DatabaseAccessError("No row was inserted"))
      }
    }.mapBoth(error => DatabaseAccessError(error.getMessage), _ => user.id)
  override def get(id: String): IO[DatabaseAccessError, Option[User]] =
    ZIO
      .fromOption(DB.readOnly[Option[User]] { implicit session =>
        sql"""select id, username,password, email, phone_number
             from user
             where id = $id
           """.map { rs =>
          User(
            rs.string("id"),
            rs.string("username"),
            Password(rs.string("password")),
            rs.stringOpt("email").map(Email(_)),
            rs.stringOpt("phone_number").map(PhoneNumber(_))
          )
        }
          .single()
          .apply()
      })
      .mapBoth(error => DatabaseAccessError(error.get), Some(_))

  override def getByUsername(username: String): IO[DatabaseAccessError, Option[User]] =
    blocking.effectBlocking {
      DB.readOnly[Option[User]] { implicit session =>
        sql"""select id, username, password, email, phone_number
                     from user
                     where username = $username
                   """.map { rs =>
          User(
            rs.string("id"),
            rs.string("username"),
            Password(rs.string("password")),
            rs.stringOpt("email").map(Email(_)),
            rs.stringOpt("phone_number").map(PhoneNumber(_))
          )
        }
          .single()
          .apply()
      }
    }.mapError(error => DatabaseAccessError(error.getMessage))

}
