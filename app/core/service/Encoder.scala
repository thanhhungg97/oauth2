package core.service

import zio.UIO

trait Encoder {
  def encode(any: String): UIO[String]

}

object Encoder {
}


