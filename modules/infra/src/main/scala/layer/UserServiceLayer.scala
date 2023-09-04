package layer

import encoder.StringEncoder
import repository.{UserMysqlRepository, UserRepository}
import service.{Encoder, PasswordPolicy, PasswordService, UserManagementService}
import zio.Function0ToLayerSyntax
import zio.blocking.Blocking

object UserServiceLayer {
  private val encoderLayer         = (StringEncoder.apply _).toLayer[Encoder]
  private val passwordServiceLayer = encoderLayer ++ PasswordPolicy.layer >>> PasswordService.layer

  private val userRepositoryLayer = Blocking.live >>> (UserMysqlRepository.apply _).toLayer[UserRepository]
  val userServiceLayer            = passwordServiceLayer ++ userRepositoryLayer ++ LogLayer.live >>> UserManagementService.pure

}
