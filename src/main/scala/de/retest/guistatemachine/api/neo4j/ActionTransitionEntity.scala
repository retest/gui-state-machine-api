package de.retest.guistatemachine.api.neo4j
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.ogm.annotation.{EndNode, Index, RelationshipEntity, StartNode}

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutState, e: SutState, a: Action) extends Entity {

  def this() = this(null, null, null)

  @Index
  @StartNode val start: SutState = s

  @Index
  @EndNode val end: SutState = e

  @Index
  // TODO #19 We need the previous SutState for the conversion back to the action since we rely on the retest ID only to keep the action small.
  //@Convert(classOf[ActionConverter])
  val actionXML: String = new ActionConverter().toGraphProperty(a)

  /// The number of times this action has been executed.
  var counter: Int = 1
}
