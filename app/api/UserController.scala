package api

import api.format.UserFormat._
import core.command.request.CreateUserRequest
import core.error.DataValidationError
import core.service.UserManagementService
import infra.module.AppContext.HttpRuntime
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}
import zio._

import javax.inject.{Inject, Singleton}
@Singleton
class UserController @Inject() (controllerComponents: ControllerComponents)(implicit runtime: HttpRuntime)
    extends AbstractController(controllerComponents) {
  private def jsonValidation[A](jsValue: JsValue)(implicit
    reads: Reads[A]
  ): IO[DataValidationError, A] =
    ZIO
      .fromEither(jsValue.validate[A].asEither)
      .mapError(e => DataValidationError(e.toString))
  def registerUser(): Action[JsValue] = Action(parse.json).async { request =>
    val data = for {
      maybeRq <- jsonValidation[CreateUserRequest](request.body)
      created <- UserManagementService.create(maybeRq)
    } yield created
    val result = data.fold(
      e => BadRequest(Json.obj("error" -> e.message)),
      oauthId => Ok(Json.toJson(oauthId))
    )
    runtime.unsafeRunToFuture(result)
  }

  def getUser(id: String): Action[AnyContent] = Action(parse.json).async {
    val data = for {
      maybeOauthClient <- UserManagementService.get(id)
    } yield maybeOauthClient
    val result: URIO[Has[UserManagementService], Result] = data.fold(
      e => BadRequest(Json.obj("error" -> e.message)),
      {
        case Some(value) => Ok(Json.toJson(value))
        case None        => BadRequest(Json.obj("error" -> "NOT_FOUND"))
      }
    )
    runtime.unsafeRunToFuture(result)
  }
}
