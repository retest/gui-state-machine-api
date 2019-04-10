package de.retest.guistatemachine.api.neo4j
import org.neo4j.ogm.annotation._

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutStateEntity, e: SutStateEntity, a: String) {

  def this() = this(null, null, null)

  @Id
  @GeneratedValue
  var id: java.lang.Long = null

  @Index
  @StartNode var start: SutStateEntity = s

  @Index
  @EndNode var end: SutStateEntity = e

  @Index
  var action: String = a

  /// The number of times this action has been executed.
  var counter: Int = 1
}
