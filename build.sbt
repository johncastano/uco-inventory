import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "uco.inventory",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "inventory",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases"),
      Resolver.bintrayRepo("websudos", "oss-releases")
    ),
    libraryDependencies ++= Seq(
      "com.outworkers" %% "phantom-dsl" % "2.14.5",
      "com.outworkers" %% "phantom-jdk8" % "2.14.5",
      "com.outworkers" %% "phantom-streams" % "2.14.5",
      scalaTest % Test

    )
  )
