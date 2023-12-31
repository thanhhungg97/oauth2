package service

import common.Telemetry
import domain.{Email, PhoneNumber, User}
import error.{PasswordError, UserManagementServiceError, UserRepositoryError}
import command.CreateUserRequest
import repository.UserRepository
import zio.logging.Logger
import zio.{Has, IO, URLayer, ZIO}

import java.util.UUID

trait UserManagementService {
  def create(command: CreateUserRequest): IO[UserManagementServiceError, String]

  def get(id: String): IO[UserManagementServiceError, Option[User]]

}

case class UserManagementServiceImpl(
  userRepository: UserRepository,
  passwordService: PasswordService,
  logging: Logger[String]
) extends UserManagementService
    with Telemetry {

  override def create(command: CreateUserRequest): IO[UserManagementServiceError, String] =
    log("UserManagementService.create") {
      val maybeUser = for {
        password <- passwordService.encode(command.password).mapError(handlePasswordServiceError)
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
        result <- userRepository.save(user).mapError(handleUserRepositoryError)
      } yield result
    }

  override def get(id: String): IO[UserManagementServiceError, Option[User]] = log("UserManagementService.get") {
    userRepository.get(id).mapError(handleUserRepositoryError)

  }

  private def validateDuplicateUserName(userName: String): IO[UserManagementServiceError, Unit] =
    for {
      maybeUser <- userRepository.getByUsername(userName).mapError(handleUserRepositoryError)
      _         <- ZIO.cond(maybeUser.isEmpty, (), UserManagementServiceError.DuplicateUserName(userName))
    } yield ()

  private def handlePasswordServiceError(e: PasswordError): UserManagementServiceError.PasswordServiceError =
    UserManagementServiceError.PasswordServiceError(e)
  private def handleUserRepositoryError(error: UserRepositoryError): UserManagementServiceError.RepositoryError =
    UserManagementServiceError.RepositoryError(error)
}

object UserManagementService {
  val pure
    : URLayer[Has[UserRepository] with Has[PasswordService] with Has[Logger[String]], Has[UserManagementService]] =
    (UserManagementServiceImpl.apply _).toLayer[UserManagementService]

  def create(command: CreateUserRequest): ZIO[Has[UserManagementService], UserManagementServiceError, String] =
    ZIO.serviceWith[UserManagementService](_.create(command))
  def get(id: String): ZIO[Has[UserManagementService], UserManagementServiceError, Option[User]] =
    ZIO.serviceWith[UserManagementService](_.get(id))

}
