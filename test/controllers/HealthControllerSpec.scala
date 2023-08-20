package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HealthControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HealthController GET" should {

    "respond OK" in {
      val controller             = new UserController(Helpers.stubControllerComponents())(null)
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val bodyText: String       = contentAsString(result)
      bodyText mustBe "ok"
    }
  }
}
