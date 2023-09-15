import play.api.mvc.{Action, ActionBuilder, BodyParser, Result}
import zio._

package object controllers {
  implicit final class ActionBuilderOps[+R[_], B](private val ab: ActionBuilder[R, B]) {
    def asyncZio[Env <: Has[_]](f: R[B] => ZIO[Env, Result, Result])(implicit runtime: Runtime[Env]): Action[B] =
      ab.async(request => runtime.unsafeRunToFuture(f(request).either.map(_.merge)))

    def asyncZio[A, Env <: Has[_]](
      bp: BodyParser[A]
    )(f: R[A] => ZIO[Env, Result, Result])(implicit runtime: Runtime[Env]): Action[A] = ab.async[A](bp) { request =>
      runtime.unsafeRunToFuture(f(request).either.map(_.merge))
    }
  }

}
