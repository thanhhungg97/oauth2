package controllers

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.collection.mutable
import scala.concurrent.Future

class UserControllerSpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {
  "A Stack" must {
    "pop values in last-in-first-out order" in {
      val stack = new mutable.Stack[Int]
      stack.push(1)
      stack.push(2)
      stack.pop() mustBe 2
      stack.pop() mustBe 1
    }
    "throw NoSuchElementException if an empty stack is popped" in {
      val emptyStack = new mutable.Stack[Int]
      a[NoSuchElementException] must be thrownBy {
        emptyStack.pop()
      }
    }
  }
  "Example Page#index" should {
    "should be valid" in {
      val controller             = new UserController(Helpers.stubControllerComponents())(null)
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val bodyText: String       = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}
