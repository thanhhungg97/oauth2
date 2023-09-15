package controllers

import command._
import controllers.format.OauthFormat._
import controllers.result.HttpResultSyntax.HttpResultSyntax
import domain.OauthId
import infra.modules.AppContext.HttpRuntime
import play.api.mvc._
import service.OauthClientService

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class OauthClientController @Inject() (
  controllerComponents: ControllerComponents
)(implicit
  runtime: HttpRuntime
) extends AbstractController(controllerComponents) {
  import controllers.result.OauthClientControllerInstance._

  def registerOauthClient(): Action[CreateOauthClientRequest] =
    Action.asyncZio(parse.json[CreateOauthClientRequest]) { request =>
      OauthClientService
        .registerOauthClient(
          request.body.redirectURI,
          request.body.scope
        )
        .fold(
          _.toResult,
          _.toResult
        )
    }

  def getOauthClient(id: UUID): Action[AnyContent] = Action.asyncZio { _ =>
    OauthClientService
      .getOauthClientConfig(OauthId(id))
      .fold(
        _.toResult,
        _.toResult
      )
  }
}
