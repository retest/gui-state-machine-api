name := "gui-state-machine-api"

organization := "retest"

scalaVersion := "2.12.7"

// Fixes serialization issues:
fork := true

// Dependencies to represent the input of states and actions:
libraryDependencies += "de.retest" % "retest-model" % "5.0.0" withSources () withJavadoc ()

// Dependencies to provide a REST service:
libraryDependencies += "com.github.scopt" % "scopt_2.12" % "3.7.0"
libraryDependencies += "io.spray" % "spray-json_2.12" % "1.3.4"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-xml" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % "test"

// Swagger:
libraryDependencies += "io.swagger" % "swagger-jaxrs" % "1.5.21"
libraryDependencies += "com.github.swagger-akka-http" %% "swagger-akka-http" % "1.0.0"

// Test frameworks:
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("de.retest.guistatemachine.rest.WebServer")
// set the main class for packaging the main jar
mainClass in (Compile, packageBin) := Some("de.retest.guistatemachine.rest.WebServer")

// format the code
scalafmtOnCompile := true

publishTo := Some("Retest Nexus" at "https://nexus.retest.org/repository/all/")

/*
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
 */

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
