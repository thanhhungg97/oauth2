package infra.repository

import core.domain.{Email, Password, PhoneNumber, User}
import core.error.DatabaseAccessError
import core.repository.UserRepository
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import zio.{IO, ZIO}

case class UserMysqlRepository() extends UserRepository {
  override def save(user: User): IO[DatabaseAccessError, Int] =
    ZIO
      .effect(DB.autoCommit[Int] { implicit session =>
        sql"""insert into user(`id`, `username`,  `password`, `email`, `phone_number`)
                   values
                   (${user.id}, ${user.username}, ${user.password},${user.email},${user.phoneNumber})
        """
          .update()
          .apply()
      })
      .mapError(error => DatabaseAccessError(error.getMessage))

  override def get(id: String): IO[DatabaseAccessError, Option[User]] =
    ZIO
      .fromOption(DB.readOnly[Option[User]] { implicit session =>
        sql"""select id, username ,password, email, phone_number
             from user
             where user.id = $id
           """.map { rs =>
          User(
            rs.string("id"),
            rs.string("username"),
            Password(rs.string("password")),
            rs.stringOpt("email").map(Email(_)),
            rs.intOpt("phone_number").map(PhoneNumber(_))
          )
        }
          .single()
          .apply()
      })
      .mapBoth(error => DatabaseAccessError(error.get), Some(_))
}
