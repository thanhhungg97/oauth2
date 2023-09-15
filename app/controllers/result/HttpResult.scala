package controllers.result

import play.api.mvc.Result
trait HttpResult[A] {
  def toResult(value: A): Result
}

object HttpResultSyntax {
  implicit final class HttpResultSyntax[A](value: A) {
    def toResult(implicit ht: HttpResult[A]) = ht.toResult(value)
  }
}
