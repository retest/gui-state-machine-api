package de.retest.guistatemachine.api.neo4j
import de.retest.guistatemachine.api.ActionIdentifier
import org.neo4j.ogm.annotation._

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutStateEntity, e: SutStateEntity, a: String, msg: String) {

  def this(s: SutStateEntity, e: SutStateEntity, a: ActionIdentifier) = this(s, e, a.hash, a.msg)
  def this() = this(null, null, null, null)

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

  @Property(name = ActionTransitionEntity.PropertyMessage)
  var message: String = msg

  /// The number of times this action has been executed.
  @Property(name = ActionTransitionEntity.PropertyNameCounter)
  var counter: Int = 1
}

object ActionTransitionEntity {
  final val PropertyNameStart = "start"
  final val PropertyNameEnd = "end"
  final val PropertyNameAction = "action"
  final val PropertyMessage = "message"
  final val PropertyNameCounter = "counter"
}
