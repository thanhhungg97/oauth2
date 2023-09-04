package domain

case class PhoneNumber private (value: String) {}

object PhoneNumber {
  def apply(maybePhoneNumber: String): PhoneNumber =
    new PhoneNumber(maybePhoneNumber)
}
