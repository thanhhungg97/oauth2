package error

import common.AppError

sealed trait OauthClientRepositoryError extends Throwable

object OauthClientRepositoryError {
  final case class ServerError(cause: Throwable)
      extends Throwable(cause)
      with OauthClientRepositoryError
      with AppError.InternalError
}
