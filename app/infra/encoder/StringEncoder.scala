package infra.encoder

import core.service.Encoder
import zio.{UIO, ZIO}

case class StringEncoder() extends Encoder {
  override def encode(any: String): UIO[String] =
    ZIO.succeed(any.hashCode.toString)
}
