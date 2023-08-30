package domain.domain

case class User(
  id: String,
  username: String,
  password: Password,
  email: Option[Email],
  phoneNumber: Option[PhoneNumber]
)
