package de.retest.guistatemachine.api.neo4j

import de.retest.surili.commons.actions.Action
import org.neo4j.graphdb.RelationshipType

case class RelationshipTypeAction(action: Action) extends RelationshipType {
  override def name() = action.toString
}
