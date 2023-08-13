import Dependencies.*

val scalaVer = "2.12.9"

val zioVersion = "1.0.18"

lazy val compileDependencies = Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-macros" % zioVersion,
) map (_ % Compile)

lazy val testDependencies = Seq(
  "dev.zio" %% "zio-test" % zioVersion,
  "dev.zio" %% "zio-test-sbt" % zioVersion,
) map (_ % Test)


lazy val settings = Seq(
  name := "oauth2",
  version := "1.0.0",
  scalaVersion := scalaVer,
  libraryDependencies ++= Zio.all ++ Logging.all ++ Database.all,
  libraryDependencies += guice,
  dependencyOverrides ++= OverrideDependencies.all,
  testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
)


lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(settings)
