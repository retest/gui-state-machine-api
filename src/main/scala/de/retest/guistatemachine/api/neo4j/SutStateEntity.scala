package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.SutStateIdentifier
import org.neo4j.ogm.annotation._

@NodeEntity
class SutStateEntity(@Property(name = SutStateEntity.PropertyNameHash)
                     @Index(unique = true)
                     var hash: java.lang.String,
                     msg: String) {

  def this(sutStateIdentifier: SutStateIdentifier) = this(sutStateIdentifier.hash, sutStateIdentifier.msg)
  def this() = this(null, null)

  @Id
  @GeneratedValue
  var id: java.lang.Long = null

  @Property(name = SutStateEntity.PropertyMessage)
  var message: String = msg

  @Relationship(`type` = "ACTIONS", direction = Relationship.INCOMING) var incomingActionTransitions = new java.util.ArrayList[ActionTransitionEntity]()
  @Relationship(`type` = "ACTIONS", direction = Relationship.OUTGOING) var outgoingActionTransitions = new java.util.ArrayList[ActionTransitionEntity]()
}

object SutStateEntity {
  final val PropertyNameHash = "hash"
  final val PropertyMessage = "message"
}
