package controllers

import command._
import controllers.format.UserFormat._
import error.UserManagementServiceError
import infra.modules.AppContext.HttpRuntime
import play.api.libs.json._
import play.api.mvc._
import service._

import javax.inject.{Inject, Singleton}
@Singleton
class UserController @Inject() (controllerComponents: ControllerComponents)(implicit runtime: HttpRuntime)
    extends AbstractController(controllerComponents) {

  def registerUser(): Action[CreateUserRequest] = Action(parse.json[CreateUserRequest]).async { request =>
    runtime.unsafeRunToFuture(UserManagementService.create(request.body).fold(handleError, toResult))
  }

  def getUser(id: String): Action[AnyContent] = Action(parse.json).async {
    runtime.unsafeRunToFuture(
      UserManagementService
        .get(id)
        .fold(
          handleError,
          {
            case Some(value) => Ok(Json.toJson(value))
            case None        => BadRequest(Json.obj("code" -> "NOT_FOUND"))
          }
        )
    )
  }
  private def toResult(id: String) = Ok(Json.obj("id" -> id))

  private def handleError(error: UserManagementServiceError): Result = error match {
    case UserManagementServiceError.PasswordServiceError(cause) =>
      BadRequest(Json.obj("code" -> "password_invalid", "message" -> cause.getMessage))
    case UserManagementServiceError.RepositoryError(cause) =>
      InternalServerError(Json.obj("code" -> "repository_error", "message" -> cause.getMessage))
    case UserManagementServiceError.DuplicateUserName(name) =>
      BadRequest(Json.obj("code" -> "duplicate_username", "message" -> s"$name is duplicated"))
  }
}
