package infra.module

import domain.repository.UserRepository
import domain.service.{Encoder, PasswordPolicy, PasswordService, UserManagementService}
import infra.encoder.StringEncoder
import infra.repository.UserMysqlRepository
import zio.Function0ToLayerSyntax
import zio.blocking.Blocking
import zio.logging.LogAnnotation
import zio.logging.slf4j.Slf4jLogger

object UserServiceLayer {
  private val encoderLayer         = (StringEncoder.apply _).toLayer[Encoder]
  private val passwordServiceLayer = encoderLayer ++ PasswordPolicy.layer >>> PasswordService.layer

  private val log = Slf4jLogger.makeWithAllAnnotationsAsMdc(excludeMdcAnnotations =
    Set(LogAnnotation.Name.name, LogAnnotation.Level.name)
  )
  private val userRepositoryLayer = Blocking.live >>> (UserMysqlRepository.apply _).toLayer[UserRepository]
  val userServiceLayer            = passwordServiceLayer ++ userRepositoryLayer ++ log >>> UserManagementService.pure

}
