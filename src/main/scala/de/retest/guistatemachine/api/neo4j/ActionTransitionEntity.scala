package de.retest.guistatemachine.api.neo4j
import org.neo4j.ogm.annotation.{EndNode, Index, RelationshipEntity, StartNode}

@RelationshipEntity(`type` = "ACTIONS")
class ActionTransitionEntity(s: SutStateEntity, e: SutStateEntity, a: String) extends Entity {

  @Index
  @StartNode val start: SutStateEntity = s

  @Index
  @EndNode val end: SutStateEntity = e

  @Index
  val action: String = a

  /// The number of times this action has been executed.
  var counter: Int = 1
}
