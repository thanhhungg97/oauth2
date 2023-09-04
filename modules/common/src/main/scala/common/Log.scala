package common

import common.Telemetry.{funcLogAnnotation, logErrorAnnotation, logErrorNameAnnotation, resultLogAnnotation}
import zio.ZIO
import zio.logging._

trait LogWithNamed[A] {

  val logging: Logger[A]

  private val named = this.getClass.getCanonicalName

  protected val namedLogging: Logger[A] = logging.named(named)

}

trait Telemetry extends LogWithNamed[String] {
  def log[R, E, A](func: String, logLevel: LogLevel = LogLevel.Debug)(body: ZIO[R, E, A]): ZIO[R, E, A] =
    namedLogging.locally(_.annotate(funcLogAnnotation, Some(func))) {
      for {
        _ <- namedLogging.log(logLevel)("started")
        r <- body.tapBoth(
               e =>
                 for {
                   _ <- namedLogging
                          .locally(
                            _.annotate(logErrorAnnotation, Some(e))
                              .annotate(logErrorNameAnnotation, Some(e))
                          )(namedLogging.log(mapErrorToLogLevel(e))("failed"))
                 } yield (),
               r =>
                 namedLogging.locally(_.annotate(resultLogAnnotation, Some(r)))(namedLogging.log(logLevel)("finished"))
             )
      } yield r
    }
  protected def mapErrorToLogLevel[E](e: E): LogLevel =
    e match {
      case _: AppError.DomainError     => LogLevel.Info
      case _: AppError.ValidationError => LogLevel.Info
      case _: AppError.ExternalError   => LogLevel.Error
      case _                           => LogLevel.Error
    }

}

object Telemetry {
  val funcLogAnnotation: LogAnnotation[Option[String]] = LogAnnotation.optional[String]("func", identity)
  val logErrorAnnotation: LogAnnotation[Option[Any]] = LogAnnotation.optional[Any](
    "error",
    {
      case error: AppError => error.getMessage
      case error           => error.toString
    }
  )
  val logErrorNameAnnotation: LogAnnotation[Option[Any]] =
    LogAnnotation.optional[Any]("errorName", _.getClass.getSimpleName)

  val resultLogAnnotation: LogAnnotation[Option[Any]] = LogAnnotation.optional[Any]("result", _.toString)

}
