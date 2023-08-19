package core.service

import core.command.request.CreateUserRequest
import core.domain.{Email, PhoneNumber, User}
import core.error.{AppError, DuplicateUserName}
import core.repository.UserRepository
import zio.{Function2ToLayerSyntax, Has, IO, URLayer, ZIO}

import java.util.UUID

trait UserManagementService {
  def create(command: CreateUserRequest): IO[AppError, Int]

  def get(id: String): IO[AppError, Option[User]]

}

object UserManagementService {
  val layer: URLayer[Has[UserRepository] with Has[PasswordService], Has[UserManagementService]] =
    (UserManagementServiceImpl.apply _).toLayer[UserManagementService]

  def create(command: CreateUserRequest): ZIO[Has[UserManagementService], AppError, Int] =
    ZIO.serviceWith[UserManagementService](_.create(command))
  def get(id: String): ZIO[Has[UserManagementService], AppError, Option[User]] =
    ZIO.serviceWith[UserManagementService](_.get(id))

}

case class UserManagementServiceImpl(userRepository: UserRepository, passwordService: PasswordService)
    extends UserManagementService {
  override def create(command: CreateUserRequest): IO[AppError, Int] = {
    val maybeUser = for {
      password <- passwordService.encode(command.password)
      userName <- validateDuplicateUserName(command.username)
    } yield User(
      UUID.randomUUID().toString,
      userName,
      password,
      command.email.map(Email(_)),
      command.phoneNumber.map(PhoneNumber(_))
    )

    for {
      user   <- maybeUser
      result <- userRepository.save(user)
    } yield result
  }

  override def get(id: String): IO[AppError, Option[User]] = userRepository.get(id)

  private def validateDuplicateUserName(userName: String): IO[AppError, String] =
    for {
      maybeUser <- userRepository.get(userName)
      result    <- ZIO.cond(maybeUser.isEmpty, userName, DuplicateUserName(userName))
    } yield result
}
