package infra.module

import core.service.OauthClientService
import infra.module.AppContext.HttpRuntime
import zio._

import javax.inject.Provider

class HttpRuntimeProvider extends Provider[HttpRuntime] {
  override def get(): HttpRuntime = {
    Runtime.unsafeFromLayer(OauthClientService.live)
  }
}
