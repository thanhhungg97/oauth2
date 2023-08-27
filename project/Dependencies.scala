import sbt.*

object Dependencies {
  object Logging {
    val zioLoggingVersion             = "0.5.16"

    val zioLoggingSlf4j = "dev.zio"             %% "zio-logging-slf4j"        % zioLoggingVersion // for zio
    val all: Seq[ModuleID] = Seq(zioLoggingSlf4j)
  }

  object Database {
    val scalikejdbc   = "org.scalikejdbc" %% "scalikejdbc"                  % "3.5.0"
    val scalikeConfig = "org.scalikejdbc" %% "scalikejdbc-config"           % "3.5.0"
    val scalikePlay   = "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5"

    val mysqlDriver        = "mysql"          % "mysql-connector-java" % "8.0.33"
    val h2db               = "com.h2database" % "h2"                   % "1.4.200"
    val all: Seq[ModuleID] = Seq(scalikejdbc, mysqlDriver, h2db, scalikeConfig, scalikePlay)
  }

  object Zio {
    val zioVersion = "1.0.18"

    val zioTest            = "dev.zio" %% "zio-test"     % zioVersion % Test
    val zioSbtTest         = "dev.zio" %% "zio-test-sbt" % zioVersion % Test
    val zio                = "dev.zio" %% "zio"          % zioVersion % Compile
    val zioMacros          = "dev.zio" %% "zio-macros"   % zioVersion
    val all: Seq[ModuleID] = Seq(zio, zioMacros, zioTest, zioSbtTest)
  }

  object PlayTest {
    val playTest = "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
    val all      = Seq(playTest)
  }
  object OverrideDependencies {
    val jacksonXml         = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.2"
    val all: Seq[ModuleID] = Seq(jacksonXml)
  }
}
