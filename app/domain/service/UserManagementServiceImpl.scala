package domain.service

import domain.command.request.CreateUserRequest
import domain.domain.{Email, PhoneNumber, User}
import domain.error.AppError
import domain.repository.{UserRepository, UserRepositoryError => URError}
import zio.{Function2ToLayerSyntax, Has, IO, URLayer, ZIO}

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
  val layer: URLayer[Has[UserRepository] with Has[PasswordService], Has[UserManagementService]] =
    (UserManagementServiceImpl.apply _).toLayer[UserManagementService]

  def create(command: CreateUserRequest): ZIO[Has[UserManagementService], UserManagementServiceError, String] =
    ZIO.serviceWith[UserManagementService](_.create(command))
  def get(id: String): ZIO[Has[UserManagementService], UserManagementServiceError, Option[User]] =
    ZIO.serviceWith[UserManagementService](_.get(id))

}

case class UserManagementServiceImpl(userRepository: UserRepository, passwordService: PasswordService)
    extends UserManagementService {
  override def create(command: CreateUserRequest): IO[UserManagementServiceError, String] = {

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

  override def get(id: String): IO[UserManagementServiceError, Option[User]] =
    userRepository.get(id).mapError(handleUserRepositoryError)

  private def validateDuplicateUserName(userName: String): IO[UserManagementServiceError, Unit] =
    for {
      maybeUser <- userRepository.getByUsername(userName).mapError(handleUserRepositoryError)
      _         <- ZIO.cond(maybeUser.isEmpty, (), UserManagementServiceError.DuplicateUserName(userName))
    } yield ()

  def handlePasswordServiceError(e: PasswordError): UserManagementServiceError.PasswordServiceError =
    UserManagementServiceError.PasswordServiceError(e)
  def handleUserRepositoryError(error: URError): UserManagementServiceError.UserRepositoryError =
    UserManagementServiceError.UserRepositoryError(error)
}
