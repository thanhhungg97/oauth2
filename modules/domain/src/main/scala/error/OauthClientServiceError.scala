package error

sealed trait OauthClientServiceError extends Throwable

object OauthClientServiceError {
  final case class RepositoryError(cause: OauthClientRepositoryError)
      extends Throwable(cause)
      with OauthClientServiceError
}
