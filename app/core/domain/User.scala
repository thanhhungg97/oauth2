package core.domain

case class PhoneNumber private (value: String) {}

object PhoneNumber {
  def apply(maybePhoneNumber: String): PhoneNumber =
    new PhoneNumber(maybePhoneNumber)
}

case class Email private (value: String)

object Email {
  def apply(maybeEmail: String): Email =
    new Email(maybeEmail)
}

case class Password private (value: String) {}

object Password {
  def apply(maybePassword: String): Password = new Password(maybePassword)
}

case class User(
  id: String,
  username: String,
  password: Password,
  email: Option[Email],
  phoneNumber: Option[PhoneNumber]
)
