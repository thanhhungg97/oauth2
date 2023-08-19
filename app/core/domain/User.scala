package core.domain

case class PhoneNumber private (value: Int) {}

object PhoneNumber {
  def apply(maybePhoneNumber: Int): PhoneNumber =
    PhoneNumber(maybePhoneNumber)
}

case class Email private (value: String)

object Email {
  def apply(maybeEmail: String): Email =
    Email(maybeEmail)
}

case class Password private (password: String) {
  override def toString = s"Reacted"
}

object Password {
  def apply(maybePassword: String): Password = Password(maybePassword)
}

case class User(
  id: String,
  username: String,
  password: Password,
  email: Option[Email],
  phoneNumber: Option[PhoneNumber]
)
