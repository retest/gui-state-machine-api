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

// Resolve dependencies from ReTest Nexus:
resolvers += "ReTest Nexus" at "https://nexus.retest.org/repository/all/"

// Dependencies to represent states and actions:
libraryDependencies += "de.retest" % "surili-commons" % "0.14.0" % "provided" withSources () withJavadoc () changing ()

// Dependencies to write GML files for yEd:
libraryDependencies += "com.github.systemdir.gml" % "GMLWriterForYed" % "2.1.0"
libraryDependencies += "org.jgrapht" % "jgrapht-core" % "1.0.1"

// Logging:
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

// Test frameworks:
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

// Format the code:
scalafmtOnCompile := true

// Publish to ReTest Nexus:
publishTo := {
  val nexus = "https://nexus.retest.org/repository/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "surili-snapshot")
  } else {
    Some("releases" at nexus + "surili")
  }
}

// Get ReTest Nexus password from environment:
sys.env.get("RETEST_NEXUS_PASSWORD") match {
  case Some(password) =>
    credentials += Credentials("ReTest Nexus", "nexus.retest.org", "retest", password)
  case _ =>
    throw new IllegalStateException("Please provide the password for retest-nexus.")
}
