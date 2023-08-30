package domain.service

import domain.error.PasswordPolicyError.InvalidPassword
import zio.test.Assertion._
import zio.test._

object SimplePasswordPolicySpec extends DefaultRunnableSpec {
  override def spec: ZSpec[environment.TestEnvironment, Any] =
    suite("PasswordPolicySpec")(
      testM("Password length less than 6, return invalid password") {
        for {
          result <- PasswordPolicy.validate("test").provideLayer(PasswordPolicy.layer).run
        } yield assert(result)(fails(equalTo(InvalidPassword("Password length is not enough"))))

      },
      testM("Password is valid, return unit") {
        val result = PasswordPolicy.validate("test12345").provideLayer(PasswordPolicy.layer)
        assertM(result)(isUnit)
      },
      testM("Password is empty, return invalid password") {
        for {
          result <- PasswordPolicy.validate("").provideLayer(PasswordPolicy.layer).run
        } yield assert(result)(fails(equalTo(InvalidPassword("Password is empty"))))
      }
    )
}
