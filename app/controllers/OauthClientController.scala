package controllers

import controllers.format.OauthClientFormat._
import controllers.format.OauthFormat._
import domain.command.request.CreateOauthClientRequest
import domain.domain.OauthId
import domain.error.OauthClientServiceError
import domain.service.OauthClientService
import infra.module.AppContext.HttpRuntime
import play.api.libs.json._
import play.api.mvc._

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class OauthClientController @Inject() (
  controllerComponents: ControllerComponents
)(implicit
  runtime: HttpRuntime
) extends AbstractController(controllerComponents) {

  def registerOauthClient(): Action[CreateOauthClientRequest] = Action(parse.json[CreateOauthClientRequest]).async {
    request: Request[CreateOauthClientRequest] =>
      runtime.unsafeRunToFuture(
        OauthClientService
          .registerOauthClient(
            request.body.redirectURI,
            request.body.scope
          )
          .fold(
            handleError,
            toResult
          )
      )
  }

  def getOauthClient(id: UUID): Action[AnyContent] = Action(parse.json).async {
    runtime.unsafeRunToFuture(
      OauthClientService
        .getOauthClientConfig(OauthId(id))
        .fold(
          handleError,
          {
            case Some(value) => Ok(Json.toJson(value))
            case None        => BadRequest(Json.obj("code" -> "NOT_FOUND"))
          }
        )
    )
  }
  private def toResult(oauthId: OauthId) = Ok(Json.toJson(oauthId))
  private def handleError(error: OauthClientServiceError): Result = error match {
    case OauthClientServiceError.RepositoryError(_) =>
      InternalServerError(Json.obj("code" -> "repository_error", "message" -> "internal error"))
  }
}
