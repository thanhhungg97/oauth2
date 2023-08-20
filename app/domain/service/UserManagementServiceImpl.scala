package domain.service

import domain.command.request.CreateUserRequest
import domain.domain.{Email, PhoneNumber, User}
import domain.error.{AppError, DuplicateUserName}
import domain.repository.UserRepository
import zio.{Function2ToLayerSyntax, Has, IO, URLayer, ZIO}

import java.util.UUID

trait UserManagementService {
  def create(command: CreateUserRequest): IO[AppError, String]

  def get(id: String): IO[AppError, Option[User]]

}

object UserManagementService {
  val layer: URLayer[Has[UserRepository] with Has[PasswordService], Has[UserManagementService]] =
    (UserManagementServiceImpl.apply _).toLayer[UserManagementService]

  def create(command: CreateUserRequest): ZIO[Has[UserManagementService], AppError, String] =
    ZIO.serviceWith[UserManagementService](_.create(command))
  def get(id: String): ZIO[Has[UserManagementService], AppError, Option[User]] =
    ZIO.serviceWith[UserManagementService](_.get(id))

}

case class UserManagementServiceImpl(userRepository: UserRepository, passwordService: PasswordService)
    extends UserManagementService {
  override def create(command: CreateUserRequest): IO[AppError, String] = {

    val maybeUser = for {
      password <- passwordService.encode(command.password)
      _        <- validateDuplicateUserName(command.username)
    } yield User(
      UUID.randomUUID().toString,
      command.username,
      password,
      command.email.map(Email(_)),
      command.phoneNumber.map(PhoneNumber(_))
    )

    for {
      user   <- maybeUser
      _      <- ZIO.debug(user)
      result <- userRepository.save(user)
    } yield result
  }

  override def get(id: String): IO[AppError, Option[User]] = userRepository.get(id)

  private def validateDuplicateUserName(userName: String): IO[AppError, Unit] =
    for {
      maybeUser <- userRepository.getByUsername(userName)
      _         <- ZIO.cond(maybeUser.isEmpty, (), DuplicateUserName(userName))
    } yield ()
}
