// create fat JAR file with all dependencies
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
// code formatting
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.1")
// signed releases
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
// release helper
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")
// static analysis
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
// coverage measurements
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
