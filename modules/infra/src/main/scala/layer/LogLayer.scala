package layer

import zio.logging.LogAnnotation
import zio.logging.slf4j.Slf4jLogger

object LogLayer {
  val live = Slf4jLogger.makeWithAllAnnotationsAsMdc(excludeMdcAnnotations =
    Set(LogAnnotation.Name.name, LogAnnotation.Level.name)
  )
}
