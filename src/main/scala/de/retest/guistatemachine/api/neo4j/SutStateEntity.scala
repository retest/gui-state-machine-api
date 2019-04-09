package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.SutStateIdentifier
import org.neo4j.ogm.annotation.{Relationship, _}

import scala.collection.mutable

@NodeEntity
class SutStateEntity(
    @Id
    //@Index(unique = true)
    val id: java.lang.String) {

  def this(sutStateIdentifier: SutStateIdentifier) = this(sutStateIdentifier.hash)
  def this() = this("")

  @Relationship(`type` = "ACTIONS", direction = Relationship.INCOMING) val incomingActionTransitions = mutable.HashSet[ActionTransitionEntity]()
  @Relationship(`type` = "ACTIONS", direction = Relationship.OUTGOING) val outgoingActionTransitions = mutable.HashSet[ActionTransitionEntity]()
}
