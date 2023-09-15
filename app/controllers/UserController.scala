package controllers

import command._
import controllers.format.UserFormat._
import controllers.result.HttpResultSyntax.HttpResultSyntax
import infra.modules.AppContext.HttpRuntime
import play.api.mvc._
import service._

import javax.inject.{Inject, Singleton}
@Singleton
class UserController @Inject() (controllerComponents: ControllerComponents)(implicit runtime: HttpRuntime)
    extends AbstractController(controllerComponents) {
  import controllers.result.UserHttpResultInstance._
  def registerUser(): Action[CreateUserRequest] = Action.asyncZio(parse.json[CreateUserRequest]) { request =>
    UserManagementService.create(request.body).fold(_.toResult, _.toResult)
  }

  def getUser(id: String): Action[AnyContent] = Action.asyncZio { _ =>
    UserManagementService
      .get(id)
      .fold(
        _.toResult,
        _.toResult
      )
  }

}
