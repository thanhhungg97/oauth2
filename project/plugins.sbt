ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
) // work around for playframework

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.19")
