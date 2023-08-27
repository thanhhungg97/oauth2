package controllers

import controllers.format.OauthClientFormat._
import controllers.format.OauthFormat._
import domain.command.request.CreateOauthClientRequest
import domain.domain.OauthId
import domain.service.{OauthClientService, OauthClientServiceError}
import infra.module.AppContext.HttpRuntime
import play.api.libs.json._
import play.api.mvc._
import zio._

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
      val result: ZIO[Has[OauthClientService], OauthClientServiceError, OauthId] =
        OauthClientService.registerOauthClient(
          request.body.redirectURI,
          request.body.scope
        )

      runtime.unsafeRunToFuture(
        result.fold(
          handleError,
          toResult
        )
      )
  }

  def getOauthClient(id: UUID): Action[AnyContent] = Action(parse.json).async {
    val result = OauthClientService.getOauthClientConfig(OauthId(id))

    runtime.unsafeRunToFuture(
      result.fold(
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
    case OauthClientServiceError.RepositoryError(cause) =>
      InternalServerError(Json.obj("code" -> "repository_error", "message" -> cause.getMessage))
  }
}
