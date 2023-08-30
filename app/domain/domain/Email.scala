package domain.domain

case class Email private (value: String)

object Email {
  def apply(maybeEmail: String): Email =
    new Email(maybeEmail)
}
