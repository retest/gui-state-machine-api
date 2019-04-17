package de.retest.guistatemachine.api.neo4j
import de.retest.guistatemachine.api.{ActionIdentifier, ActionTransitions, State, SutStateIdentifier}
import org.neo4j.ogm.session.Session

import scala.collection.JavaConversions._
import scala.collection.immutable.HashMap

case class StateNeo4J(sutStateIdentifier: SutStateIdentifier, guiStateMachine: GuiStateMachineNeo4J) extends State {

  override def getSutStateIdentifier: SutStateIdentifier = sutStateIdentifier
  override def getOutgoingActionTransitions: Map[ActionIdentifier, ActionTransitions] =
    Neo4JUtil.transaction { session =>
      val sutStateEntity = getSutStateEntity(session)
      var result = HashMap[ActionIdentifier, ActionTransitions]()
      val iterator = sutStateEntity.outgoingActionTransitions.iterator()
      while (iterator.hasNext) {
        val relationship = iterator.next()
        val action = new ActionIdentifier(relationship.action, relationship.message)
        val targetSutState = new SutStateIdentifier(relationship.end.hash)
        val actionTransitions = if (result.contains(action)) {
          val existing = result(action)
          ActionTransitions(existing.states ++ Set(StateNeo4J(targetSutState, guiStateMachine)))
        } else {
          ActionTransitions(Set(StateNeo4J(targetSutState, guiStateMachine)))
        }
        result = result + (action -> actionTransitions)
      }
      result
    }(guiStateMachine.sessionFactory)

  def getIncomingActionTransitions: Map[ActionIdentifier, ActionTransitions] =
    Neo4JUtil.transaction { session =>
      val sutStateEntity = getSutStateEntity(session)
      var result = HashMap[ActionIdentifier, ActionTransitions]()
      val iterator = sutStateEntity.incomingActionTransitions.iterator()
      while (iterator.hasNext) {
        val relationship = iterator.next()
        val action = new ActionIdentifier(relationship.action, relationship.message)
        val sourceSutState = new SutStateIdentifier(relationship.start.hash)
        val actionTransitions = if (result.contains(action)) {
          val existing = result(action)
          ActionTransitions(existing.states ++ Set(StateNeo4J(sourceSutState, guiStateMachine)))
        } else {
          ActionTransitions(Set(StateNeo4J(sourceSutState, guiStateMachine)))
        }
        result = result + (action -> actionTransitions)
      }
      result
    }(guiStateMachine.sessionFactory)

  private[api] override def addTransition(a: ActionIdentifier, to: State): Unit =
    Neo4JUtil.transaction { session =>
      val sourceState = getSutStateEntity(session)
      val targetSutStateIdentifier = to.asInstanceOf[StateNeo4J].sutStateIdentifier
      val targetState = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, targetSutStateIdentifier)
      val matchingTransitions =
        sourceState.outgoingActionTransitions.toSeq.filter(actionTransition => actionTransition.end == targetState && actionTransition.action == a.hash)

      if (matchingTransitions.nonEmpty) {
        val first = matchingTransitions.head
        session.save(first)
      } else {
        val transition = new ActionTransitionEntity(sourceState, targetState, a)
        session.save(transition)
      }
    }(guiStateMachine.sessionFactory)

  private def getSutStateEntity(session: Session): SutStateEntity = guiStateMachine.getNodeBySutStateIdentifierOrThrow(session, sutStateIdentifier)
}
