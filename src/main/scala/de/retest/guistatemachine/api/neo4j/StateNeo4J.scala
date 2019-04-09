package de.retest.guistatemachine.api.neo4j
import de.retest.guistatemachine.api.{ActionIdentifier, ActionTransitions, State, SutStateIdentifier}
import org.neo4j.ogm.cypher.{ComparisonOperator, Filter}

import scala.collection.immutable.HashMap

case class StateNeo4J(sutStateIdentifier: SutStateIdentifier, guiStateMachine: GuiStateMachineNeo4J) extends State {
  implicit val session = guiStateMachine.session

  override def getSutStateIdentifier: SutStateIdentifier = sutStateIdentifier
  // TODO #19 Can we somehow convert the outgoing relations directly from the SutStateEntity?
  override def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions] =
    Neo4jSessionFactory.transaction {
      val filter = new Filter("start", ComparisonOperator.EQUALS, sutStateIdentifier.hash)
      val transitions = session.loadAll(classOf[ActionTransitionEntity], filter)
      var result = HashMap[ActionIdentifier, ActionTransitions]()
      val iterator = transitions.iterator()
      while (iterator.hasNext) {
        val relationship = iterator.next()
        val action = new ActionIdentifier(relationship.action)
        val targetSutState = new SutStateIdentifier(relationship.end.id)
        val counter = relationship.counter
        val actionTransitions = if (result.contains(action)) {
          val existing = result(action)
          ActionTransitions(existing.states ++ Set(StateNeo4J(targetSutState, guiStateMachine)), existing.executionCounter + counter)
        } else {
          ActionTransitions(Set(StateNeo4J(targetSutState, guiStateMachine)), counter)
        }
        result = result + (action -> actionTransitions)
      }
      result
    }
  // TODO #19 Can we somehow convert the incoming relations directly from the SutStateEntity?
  def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions] = Neo4jSessionFactory.transaction {
    val filter = new Filter("end", ComparisonOperator.EQUALS, sutStateIdentifier.hash)
    val transitions = session.loadAll(classOf[ActionTransitionEntity], filter)
    var result = HashMap[ActionIdentifier, ActionTransitions]()
    val iterator = transitions.iterator()
    while (iterator.hasNext) {
      val relationship = iterator.next()
      val action = new ActionIdentifier(relationship.action)
      val sourceSutState = new SutStateIdentifier(relationship.start.id)
      val counter = relationship.counter
      val actionTransitions = if (result.contains(action)) {
        val existing = result(action)
        ActionTransitions(existing.states ++ Set(StateNeo4J(sourceSutState, guiStateMachine)), existing.executionCounter + counter)
      } else {
        ActionTransitions(Set(StateNeo4J(sourceSutState, guiStateMachine)), counter)
      }
      result = result + (action -> actionTransitions)
    }
    result
  }

  private[api] override def addTransition(a: ActionIdentifier, to: State): Int = Neo4jSessionFactory.transaction {
    /*
    TODO #19 Filter for start and end states.
    val filterStart = new Filter("start", ComparisonOperator.EQUALS, sutStateIdentifier.hash)
    val filterEnd = new Filter("end", ComparisonOperator.EQUALS, targetSutStateIdentifier.hash)
     filterStart.and(filterAction).and(filterEnd)
     */

    val filterAction = new Filter("action", ComparisonOperator.EQUALS, a.hash)
    val targetSutStateIdentifier = to.asInstanceOf[StateNeo4J].sutStateIdentifier

    import scala.collection.JavaConversions._
    val transitions = session.loadAll(classOf[ActionTransitionEntity], filterAction).toSeq

    val matchingTransitions = transitions.filter(actionTransitionEntity =>
      actionTransitionEntity.start.id == sutStateIdentifier.hash && actionTransitionEntity.end.id == targetSutStateIdentifier.hash)
    if (matchingTransitions.nonEmpty) {
      val first: ActionTransitionEntity = matchingTransitions.head
      first.counter = first.counter + 1
      session.save(first)
      first.counter
    } else {
      val sourceState = guiStateMachine.getNodeBySutStateIdentifier(sutStateIdentifier).get
      val targetState = guiStateMachine.getNodeBySutStateIdentifier(targetSutStateIdentifier).get
      val transition = new ActionTransitionEntity(sourceState, targetState, a.hash)
      session.save(transition)
      1
    }
  }
}
