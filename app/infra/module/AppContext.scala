package infra.module

import core.service.OauthClientService
import zio.{Has, _}

object AppContext {
  type AppContext  = Has[OauthClientService]
  type HttpRuntime = Runtime[AppContext]
}
