package domain.service

import zio.UIO

trait Encoder {
  def encode(any: String): UIO[String]

}
