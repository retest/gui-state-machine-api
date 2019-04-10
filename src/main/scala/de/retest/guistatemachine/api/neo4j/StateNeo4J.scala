package de.retest.guistatemachine.api.neo4j
import de.retest.guistatemachine.api.{ActionIdentifier, ActionTransitions, State, SutStateIdentifier}

import scala.collection.JavaConversions._
import scala.collection.immutable.HashMap

case class StateNeo4J(sutStateIdentifier: SutStateIdentifier, guiStateMachine: GuiStateMachineNeo4J) extends State {

  override def getSutStateIdentifier: SutStateIdentifier = sutStateIdentifier
  override def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions] =
    Neo4jSessionFactory.transaction { session =>
      val sutStateEntity = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, sutStateIdentifier)
      var result = HashMap[ActionIdentifier, ActionTransitions]()
      val iterator = sutStateEntity.outgoingActionTransitions.iterator()
      while (iterator.hasNext) {
        val relationship = iterator.next()
        val action = new ActionIdentifier(relationship.action)
        val targetSutState = new SutStateIdentifier(relationship.end.hash)
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
    }(guiStateMachine.uri)

  def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions] =
    Neo4jSessionFactory.transaction { session =>
      val sutStateEntity = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, sutStateIdentifier)
      var result = HashMap[ActionIdentifier, ActionTransitions]()
      val iterator = sutStateEntity.incomingActionTransitions.iterator()
      while (iterator.hasNext) {
        val relationship = iterator.next()
        val action = new ActionIdentifier(relationship.action)
        val sourceSutState = new SutStateIdentifier(relationship.start.hash)
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
    }(guiStateMachine.uri)

  private[api] override def addTransition(a: ActionIdentifier, to: State): Int =
    Neo4jSessionFactory.transaction { session =>
      val sourceState = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, sutStateIdentifier)
      val targetSutStateIdentifier = to.asInstanceOf[StateNeo4J].sutStateIdentifier
      val targetState = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, targetSutStateIdentifier)
      val matchingTransitions =
        sourceState.outgoingActionTransitions.toSeq.filter(actionTransition => actionTransition.end == targetState && actionTransition.action == a.hash)

      if (matchingTransitions.nonEmpty) {
        val first = matchingTransitions.head
        first.counter = first.counter + 1
        session.save(first)
        first.counter
      } else {
        val transition = new ActionTransitionEntity(sourceState, targetState, a.hash)
        session.save(transition)
        1
      }
    }(guiStateMachine.uri)
}
