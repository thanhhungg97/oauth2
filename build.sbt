import Dependencies.*
import sbt.Keys.testFrameworks

val scalaVer = "2.12.9"


lazy val commonSettings = Seq(
  organization := "com.scala.oauth",
  version      := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.9",
  scalacOptions := Seq(
    "-deprecation",
    "-feature"
  )
)

lazy val common = project
  .in(file("modules/common"))
  .settings(
    name := "common",
    commonSettings,
    libraryDependencies ++= Zio.all ++ Logging.all,
  )

lazy val domain = project
  .in(file("modules/domain"))
  .dependsOn(common)
  .settings(
    name := "domain",
    commonSettings,
    testFrameworks ++= Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    libraryDependencies ++= Zio.all,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

  )

lazy val infra = project
  .in(file("modules/infra"))
  .dependsOn(domain, common)
  .settings(name := "infra", commonSettings, libraryDependencies ++= Database.all)

lazy val api = (project in file("."))
  .enablePlugins(PlayScala)
  .aggregate(domain, infra, common)
  .dependsOn(domain, infra, common)
  .settings(
    Seq(
      name         := "oauth2",
      version      := "1.0.0",
      scalaVersion := scalaVer,
      libraryDependencies ++= Logging.all ++ PlayTest.all,
      libraryDependencies += guice,
      dependencyOverrides ++= OverrideDependencies.all
    )
  )
