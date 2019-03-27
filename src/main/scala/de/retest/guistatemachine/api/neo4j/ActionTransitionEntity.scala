package de.retest.guistatemachine.api.neo4j
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.neo4j.ogm.annotation.{EndNode, RelationshipEntity, StartNode}

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutState, e: SutState, a: Action) extends Entity {

  def this() = this(null, null, null)

  @StartNode val start: SutState = s

  @EndNode val end: SutState = e

  @Convert(classOf[ActionConverter])
  val action: Action = a

  /// The number of times this action has been executed.
  var counter: Int = 1
}
