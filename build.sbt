name := "gui-state-machine-api"

organization := "de.retest"

scalaVersion := "2.12.8"

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

// Dependencies to write GraphML files for yEd:
libraryDependencies += "com.github.systemdir.gml" % "GMLWriterForYed" % "2.1.0"
libraryDependencies += "org.jgrapht" % "jgrapht-core" % "1.0.1"

// Logging:
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

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
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "surili-snapshot")
  } else {
    Some("releases" at nexus + "surili")
  }
}

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
