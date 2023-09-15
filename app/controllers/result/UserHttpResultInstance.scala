package controllers.result

import domain.User
import error.UserManagementServiceError
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._
import controllers.format.UserFormat._
object UserHttpResultInstance {
  implicit val userManagementServiceErrorInstance = new HttpResult[UserManagementServiceError] {
    override def toResult(error: UserManagementServiceError): Result = error match {
      case UserManagementServiceError.PasswordServiceError(cause) =>
        BadRequest(Json.obj("code" -> "password_invalid", "message" -> cause.getMessage))
      case UserManagementServiceError.RepositoryError(cause) =>
        InternalServerError(Json.obj("code" -> "repository_error", "message" -> cause.getMessage))
      case UserManagementServiceError.DuplicateUserName(name) =>
        BadRequest(Json.obj("code" -> "duplicate_username", "message" -> s"$name is duplicated"))
    }
  }
  implicit val userResultInstance = new HttpResult[Option[User]] {
    override def toResult(value: Option[User]): Result = value match {
      case Some(value) => Ok(Json.toJson(value))
      case None        => BadRequest(Json.obj("code" -> "NOT_FOUND"))
    }
  }
  implicit val stringResultInstance = new HttpResult[String] {
    override def toResult(value: String): Result = Ok(Json.obj("id" -> value))
  }
}
