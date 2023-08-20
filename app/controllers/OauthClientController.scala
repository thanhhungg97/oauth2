package controllers

import controllers.format.OauthClientFormat._
import controllers.format.OauthFormat._
import domain.command.request.CreateOauthClientRequest
import domain.domain.OauthId
import domain.error.DataValidationError
import domain.service.OauthClientService
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

  private def jsonValidation[A](jsValue: JsValue)(implicit
    reads: Reads[A]
  ): IO[DataValidationError, A] =
    ZIO
      .fromEither(jsValue.validate[A].asEither)
      .mapError(e => DataValidationError(e.toString))

  def registerOauthClient(): Action[JsValue] = Action(parse.json).async { request =>
    val data = for {
      maybeRq <- jsonValidation[CreateOauthClientRequest](request.body)
      created <- OauthClientService.registerOauthClient(
                   maybeRq.redirectURI,
                   maybeRq.scope
                 )
    } yield created
    val result = data.fold(
      e => BadRequest(Json.obj("error" -> e.message)),
      oauthId => Ok(Json.toJson(oauthId))
    )
    runtime.unsafeRunToFuture(result)
  }

  def getOauthClient(id: UUID): Action[AnyContent] = Action(parse.json).async {
    val data = for {
      maybeOauthClient <- OauthClientService.getOauthClientConfig(OauthId(id))
    } yield maybeOauthClient
    val result: URIO[Has[OauthClientService], Result] = data.fold(
      e => BadRequest(Json.obj("error" -> e.message)),
      {
        case Some(value) => Ok(Json.toJson(value))
        case None        => BadRequest(Json.obj("error" -> "NOT_FOUND"))
      }
    )
    runtime.unsafeRunToFuture(result)
  }
}
