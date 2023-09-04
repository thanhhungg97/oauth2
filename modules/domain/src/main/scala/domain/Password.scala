package domain

case class Password private (value: String) {}

object Password {
  def apply(maybePassword: String): Password = new Password(maybePassword)
}
