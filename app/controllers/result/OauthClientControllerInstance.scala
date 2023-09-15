package controllers.result

import controllers.format.OauthClientFormat._
import controllers.format.OauthFormat._
import domain.{OauthClient, OauthId}
import error.OauthClientServiceError
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._
object OauthClientControllerInstance {
  implicit val oauthClientServiceErrorResultInstance = new HttpResult[OauthClientServiceError] {
    override def toResult(value: OauthClientServiceError): Result =
      InternalServerError(Json.obj("code" -> "repository_error", "message" -> "internal error"))
  }
  implicit val oauthIdResultInstance = new HttpResult[OauthId] {
    override def toResult(value: OauthId): Result = Ok(Json.toJson(value))
  }
  implicit val oauthClientInstance = new HttpResult[Option[OauthClient]] {
    override def toResult(value: Option[OauthClient]): Result = value match {
      case Some(value) => Ok(Json.toJson(value))
      case None        => BadRequest(Json.obj("code" -> "NOT_FOUND"))
    }
  }
}
