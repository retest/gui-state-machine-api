package de.retest.guistatemachine.api.neo4j
import org.neo4j.ogm.annotation._

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutStateEntity, e: SutStateEntity, a: String) {

  def this() = this(null, null, null)

  @Id
  @GeneratedValue
  var id: java.lang.Long = null

  @Property(name = ActionTransitionEntity.PropertyNameStart)
  @Index
  @StartNode var start: SutStateEntity = s

  @Property(name = ActionTransitionEntity.PropertyNameEnd)
  @Index
  @EndNode var end: SutStateEntity = e

  @Property(name = ActionTransitionEntity.PropertyNameAction)
  @Index
  var action: String = a

  /// The number of times this action has been executed.
  @Property(name = ActionTransitionEntity.PropertyNameCounter)
  var counter: Int = 1
}

object ActionTransitionEntity {
  final val PropertyNameStart = "start"
  final val PropertyNameEnd = "end"
  final val PropertyNameAction = "action"
  final val PropertyNameCounter = "counter"
}
