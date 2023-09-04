package controllers.modules

import controllers.modules.AppContext.HttpRuntime
import layer._
import zio._

import javax.inject.Provider

class HttpRuntimeProvider extends Provider[HttpRuntime] {
  override def get(): HttpRuntime = {
    val live =
      OauthClientRepositoryLayer.layer ++ OauthSecretGeneratorLayer.live ++ LogLayer.live >>> OauthClientServiceLayer.layer

    Runtime.unsafeFromLayer(live ++ UserServiceLayer.userServiceLayer)
  }
}
