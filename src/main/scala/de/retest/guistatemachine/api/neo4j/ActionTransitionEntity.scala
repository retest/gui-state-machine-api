package de.retest.guistatemachine.api.neo4j
import de.retest.recheck.ui.descriptors.SutState
import org.neo4j.ogm.annotation.{EndNode, RelationshipEntity, StartNode}

@RelationshipEntity(`type` = "EXECUTED")
class ActionTransitionEntity(start: SutState, end: SutState) extends Entity {

  def this() = this(null, null)

  @StartNode val s = start

  @EndNode val e = end
}
