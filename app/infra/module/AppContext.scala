package infra.module

import core.service.{OauthClientService, UserManagementService}
import zio.{Has, _}

object AppContext {
  type AppContext  = Has[OauthClientService] with Has[UserManagementService]
  type HttpRuntime = Runtime[AppContext]
}
