import Dependencies.*
import sbt.Keys.logBuffered

val scalaVer = "2.12.9"

lazy val settings = Seq(
  name         := "oauth2",
  version      := "1.0.0",
  scalaVersion := scalaVer,
  libraryDependencies ++= Zio.all ++ Logging.all ++ Database.all ++ PlayTest.all,
  libraryDependencies += guice,
  dependencyOverrides ++= OverrideDependencies.all,
  testFrameworks     := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
  logBuffered        := false)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(settings)
