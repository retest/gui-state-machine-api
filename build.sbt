name := "gui-state-machine-api"

version := "1.0"

organization := "retest"

scalaVersion := "2.12.7"

// Dependencies to represent the input of states and actions:
libraryDependencies += "de.retest" % "retest-model" % "5.0.0"
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0"

// Dependencies to provide a REST service:
libraryDependencies += "com.github.scopt" % "scopt_2.12" % "3.7.0"
libraryDependencies += "io.spray" % "spray-json_2.12" % "1.3.4"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-xml" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % "test"

// Test frameworks:
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % "test"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("de.retest.guistatemachine.rest.WebServer")
// set the main class for packaging the main jar
mainClass in (Compile, packageBin) := Some("de.retest.guistatemachine.rest.WebServer")