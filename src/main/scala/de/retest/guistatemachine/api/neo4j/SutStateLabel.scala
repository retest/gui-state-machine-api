package de.retest.guistatemachine.api.neo4j

import org.neo4j.graphdb.Label

object SutStateLabel extends Label {
  override def name() = "SutState"
}
