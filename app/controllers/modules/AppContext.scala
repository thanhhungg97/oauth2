package controllers.modules

import service.{OauthClientService, UserManagementService}
import zio.{Has, Runtime}

object AppContext {
  type AppContext  = Has[OauthClientService] with Has[UserManagementService]
  type HttpRuntime = Runtime[AppContext]
}
