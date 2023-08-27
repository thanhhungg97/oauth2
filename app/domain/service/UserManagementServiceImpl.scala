package domain.service

import domain.command.request.CreateUserRequest
import domain.domain.{Email, PhoneNumber, User}
import domain.error.AppError
import domain.repository.{UserRepository, UserRepositoryError => URError}
import zio.logging.{LogAnnotation, Logger}
import zio.{Has, IO, URLayer, ZIO}

import java.util.UUID

trait UserManagementService {
  def create(command: CreateUserRequest): IO[UserManagementServiceError, String]

  def get(id: String): IO[UserManagementServiceError, Option[User]]

}

sealed trait UserManagementServiceError extends Throwable

object UserManagementServiceError {
  final case class PasswordServiceError(cause: PasswordError) extends Throwable(cause) with UserManagementServiceError

  final case class UserRepositoryError(cause: URError) extends Throwable(cause) with UserManagementServiceError

  final case class DuplicateUserName(name: String)
      extends Throwable(s"Duplicate User Name: $name")
      with UserManagementServiceError
      with AppError.DomainError

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

case class UserManagementServiceImpl(
  userRepository: UserRepository,
  passwordService: PasswordService,
  log: Logger[String]
) extends UserManagementService {
  override def create(command: CreateUserRequest): IO[UserManagementServiceError, String] = {

    val maybeUser = for {
      password <- passwordService.encode(command.password).mapError(handlePasswordServiceError)
      _        <- validateDuplicateUserName(command.username)
      _ <- log.locally(LogAnnotation.Name("my-logger" :: Nil)) {
             log.warn("testLog" + password.toString) // value of LogAnnotation.Name is only visible in this block
           }

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

  override def get(id: String): IO[UserManagementServiceError, Option[User]] =
    userRepository.get(id).mapError(handleUserRepositoryError)

  private def validateDuplicateUserName(userName: String): IO[UserManagementServiceError, Unit] =
    for {
      maybeUser <- userRepository.getByUsername(userName).mapError(handleUserRepositoryError)
      _         <- ZIO.cond(maybeUser.isEmpty, (), UserManagementServiceError.DuplicateUserName(userName))
    } yield ()

  private def handlePasswordServiceError(e: PasswordError): UserManagementServiceError.PasswordServiceError =
    UserManagementServiceError.PasswordServiceError(e)
  private def handleUserRepositoryError(error: URError): UserManagementServiceError.UserRepositoryError =
    UserManagementServiceError.UserRepositoryError(error)
}
