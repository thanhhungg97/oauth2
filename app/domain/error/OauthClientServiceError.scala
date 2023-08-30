package domain.error

sealed trait OauthClientServiceError extends Throwable

object OauthClientServiceError {
  final case class RepositoryError(cause: domain.repository.OauthClientRepositoryError)
      extends Throwable(cause)
      with OauthClientServiceError
}
