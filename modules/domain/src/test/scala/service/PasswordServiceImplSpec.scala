package service

import domain.Password
import error.{PasswordError, PasswordPolicyError}
import zio.test.Assertion.{equalTo, fails}
import zio.test._
import zio.test.mock.Expectation._

object PasswordServiceImplSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("PasswordServiceSpec")(
    testM("return Encoded Password when valid Password Policy") {
      val request  = "test"
      val response = "12313"
      val mocks =
        EncoderMock.Encode(equalTo(request), value(response)) &&
          PasswordPolicyMock.Validate(equalTo(request), value(Unit))

      val result = PasswordService
        .encode(request)
        .provideLayer(mocks >>> PasswordService.layer)

      assertM(result)(equalTo(Password(response)))
    },
    testM("return InvalidPassword when PasswordPolicy return invalid") {
      val request = "test"
      val error   = PasswordPolicyError.InvalidPassword("Password length is not enough")
      val mocks =
        EncoderMock.empty ++
          PasswordPolicyMock
            .Validate(equalTo(request), failure(PasswordPolicyError.InvalidPassword("Password length is not enough")))

      val result = PasswordService
        .encode(request)
        .provideLayer(mocks >>> PasswordService.layer)

      assertM(result.run)(fails(equalTo(PasswordError.InvalidPasswordPolicy(error))))
    }
  )
}
