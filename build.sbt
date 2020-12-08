name := "simple-crud"

version := "1.0"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.1"
val slickVersion = "3.3.3"
val akkaSwagger = "2.3.0"
val logbackVersion = "1.2.3"
val pgVersion = "9.4-1206-jdbc42"
val h2Version = "1.4.192"
val scalaTestVersion = "3.2.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.megard" %% "akka-http-cors" % "1.1.0",

  //  Database
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "org.postgresql" % "postgresql" % pgVersion,

  // Swagger
  "com.github.swagger-akka-http" %% "swagger-akka-http" % akkaSwagger,
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",

  // Logging
  "ch.qos.logback" % "logback-classic" % logbackVersion,

  // Test
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "com.h2database" % "h2" % h2Version % Test
)

// ### Docker Build ###

import com.typesafe.sbt.packager.docker.DockerChmodType

enablePlugins(JavaAppPackaging, AshScriptPlugin)
dockerBaseImage := "openjdk:8-jre-alpine"
packageName in Docker := packageName.value
version in Docker := version.value
dockerExposedPorts ++= Seq(3000)
dockerChmodType := DockerChmodType.UserGroupWriteExecute
// ### END ###