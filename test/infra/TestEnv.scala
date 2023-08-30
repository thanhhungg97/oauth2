package infra

import zio.logging.Logging

object TestEnv {
  val noop = Logging.ignore

}
