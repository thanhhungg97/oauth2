package controllers.modules

import infra.modules.AppContext.HttpRuntime
import infra.layer.{LogLayer, OauthClientRepositoryLayer, OauthClientServiceLayer, OauthSecretGeneratorLayer, UserServiceLayer}
import zio._

import javax.inject.Provider

class HttpRuntimeProvider extends Provider[HttpRuntime] {
  override def get(): HttpRuntime = {
    val live =
      OauthClientRepositoryLayer.layer ++ OauthSecretGeneratorLayer.live ++ LogLayer.live >>> OauthClientServiceLayer.layer

    Runtime.unsafeFromLayer(live ++ UserServiceLayer.userServiceLayer)
  }
}
