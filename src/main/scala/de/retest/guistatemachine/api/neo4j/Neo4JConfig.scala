package de.retest.guistatemachine.api.neo4j

import java.nio.file.Path

import org.neo4j.ogm.config.Configuration

sealed trait Neo4JConfig {
  def buildConfig(): Configuration
}

case class EmbeddedConfig(path: Path) extends Neo4JConfig {
  override def buildConfig(): Configuration = new Configuration.Builder().uri(path.toUri.toString).build
}

case class BoltConfig(host: String, port: Int, user: String, password: String) extends Neo4JConfig {
  override def buildConfig(): Configuration =
    new Configuration.Builder()
      .uri(s"bolt://$host:$port")
      .credentials(user, password)
      .build()
}
