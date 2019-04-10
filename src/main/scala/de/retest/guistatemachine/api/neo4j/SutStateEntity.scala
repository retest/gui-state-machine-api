package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.SutStateIdentifier
import org.neo4j.ogm.annotation._

@NodeEntity
class SutStateEntity(
    @Property(name = SutStateEntity.PropertyNameHash)
    @Index(unique = true)
    var hash: java.lang.String) {

  def this(sutStateIdentifier: SutStateIdentifier) = this(sutStateIdentifier.hash)
  def this() = this("")

  @Id
  @GeneratedValue
  var id: java.lang.Long = null

  @Relationship(`type` = "ACTIONS", direction = Relationship.OUTGOING) var incomingActionTransitions = new java.util.LinkedList[ActionTransitionEntity]()
  @Relationship(`type` = "ACTIONS", direction = Relationship.INCOMING) var outgoingActionTransitions = new java.util.LinkedList[ActionTransitionEntity]()
}

object SutStateEntity {
  final val PropertyNameHash = "hash"
}
