name := "gui-state-machine-api"

organization := "de.retest"

scalaVersion := "2.12.7"

/*
 * retest-sut-api provides a package and an object with the name a etc.
 * We have to resolve the conflict.
 */
scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-Yresolve-term-conflict:package")

// Disable using the Scala version in output paths and artifacts:
crossPaths := false

// Fixes serialization issues:
fork := true

// Dependencies to represent states and actions:
libraryDependencies += "de.retest" % "surili-model" % "0.1.0-SNAPSHOT" withSources () withJavadoc ()
libraryDependencies += "de.retest" % "retest-sut-api" % "3.2.0" withSources () withJavadoc ()

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

// ReTest's Nexus:
publishTo := {
  val nexus = "https://nexus.retest.org/repository/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "retest-snapshot")
  else
    Some("releases" at nexus + "retest")
}

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
