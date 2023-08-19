package infra.module

import core.repository.UserRepository
import core.service.{Encoder, PasswordPolicy, PasswordService, UserManagementService}
import infra.encoder.StringEncoder
import infra.repository.UserMysqlRepository
import zio.Function0ToLayerSyntax
import zio.blocking.Blocking

object UserServiceLayer {
  private val encoderLayer         = (StringEncoder.apply _).toLayer[Encoder]
  private val passwordServiceLayer = encoderLayer ++ PasswordPolicy.layer >>> PasswordService.layer

  private val userRepositoryLayer = Blocking.live >>> (UserMysqlRepository.apply _).toLayer[UserRepository]
  val userServiceLayer            = passwordServiceLayer ++ userRepositoryLayer >>> UserManagementService.layer

}
