import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "uco.inventory",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
  name := "inventory",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases"),
    Resolver.bintrayRepo("websudos", "oss-releases"),
    Resolver.bintrayRepo("hseeberger", "maven")
  ),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.1",
    "com.typesafe.akka" %% "akka-stream" % "2.5.12",
    "com.typesafe.akka" %% "akka-actor" % "2.5.12",
    "io.circe" %% "circe-core" % "0.9.3",
    "io.circe" %% "circe-generic" % "0.9.3",
    "io.circe" %% "circe-parser" % "0.9.3",
    "de.heikoseeberger" %% "akka-http-circe" % "1.21.0",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
    "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
    "org.typelevel" %% "cats-core" % "1.1.0",
    "com.outworkers" %% "phantom-dsl" % "2.14.5",
    "com.outworkers" %% "phantom-jdk8" % "2.14.5",
    "com.outworkers" %% "phantom-streams" % "2.14.5",
    scalaTest % Test
  )
)

scalafmtOnCompile in ThisBuild := true
