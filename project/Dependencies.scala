import sbt.*

object Dependencies {
  object Logging {
    val logbackVersion = "1.4.8"
    val logstashLogbackEncoderVersion = "7.3"
    val zioLoggingVersion = "0.5.16"

    val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
    val logbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % logstashLogbackEncoderVersion
    val zioLogging = "dev.zio" %% "zio-logging" % zioLoggingVersion
    val zioLoggingSlf4j = "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion

    val all: Seq[ModuleID] = Seq(logbackClassic, logbackEncoder, zioLogging, zioLoggingSlf4j)
  }

}
