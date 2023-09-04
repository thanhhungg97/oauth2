package repository

import domain._
import error.UserRepositoryError
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.IO
import zio.blocking.Blocking

case class UserMysqlRepository(blocking: Blocking.Service) extends UserRepository {
  override def save(user: User): IO[UserRepositoryError, String] =
    blocking.effectBlocking {
      DB.autoCommit { implicit session =>
        val rowEffected =
          sql"""insert into user(`id`, `username`,  `password`, `email`, `phone_number`)
                       values
                       (${user.id}, ${user.username}, ${user.password.value},${user.email
              .map(_.value)
              .getOrElse("")},${user.phoneNumber.map(_.value).getOrElse("")})

            """
            .update()
            .apply()
        Either.cond(rowEffected == 1, user.id, UserRepositoryError.ValidationError("No row was inserted"))
      }
    }.mapBoth(UserRepositoryError.ServiceError, _ => user.id)
  override def get(id: String): IO[UserRepositoryError, Option[User]] =
    blocking.effectBlocking {
      DB.readOnly[Option[User]] { implicit session =>
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
      }
    }
      .mapError(UserRepositoryError.ServiceError)

  override def getByUsername(username: String): IO[UserRepositoryError, Option[User]] =
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
    }.mapError(UserRepositoryError.ServiceError)

}
