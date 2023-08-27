package infra.module

import infra.module.AppContext.HttpRuntime
import zio._

import javax.inject.Provider

class HttpRuntimeProvider extends Provider[HttpRuntime] {
  override def get(): HttpRuntime = {
    val live =
      OauthClientRepositoryLayer.layer ++ OauthSecretGeneratorLayer.live ++ LogLayer.live >>> OauthClientServiceLayer.layer

    Runtime.unsafeFromLayer(live ++ UserServiceLayer.userServiceLayer)
  }
}
