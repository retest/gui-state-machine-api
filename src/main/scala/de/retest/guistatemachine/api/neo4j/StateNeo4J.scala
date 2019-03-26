package de.retest.guistatemachine.api.neo4j
import de.retest.guistatemachine.api.{ActionTransitions, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.graphdb.{Direction, Node, Relationship, Transaction}

import scala.collection.immutable.HashMap

case class StateNeo4J(sutState: SutState, guiStateMachine: GuiStateMachineNeo4J) extends State {

  override def getSutState: SutState = sutState
  override def getTransitions: Map[Action, ActionTransitions] = {
    val node = getNode()
    val outgoingRelationships = node.getRelationships(Direction.OUTGOING)
    var result = HashMap[Action, ActionTransitions]()
    val iterator = outgoingRelationships.iterator()
    while (iterator.hasNext()) {
      val relationship = iterator.next()
      val relationshipTypeAction = relationship.getType.asInstanceOf[RelationshipTypeAction]
      val action = relationshipTypeAction.action
      val sutState = guiStateMachine.getSutState(relationship.getEndNode)
      val actionTransitions = if (result.contains(action)) {
        val existing = result.get(action).get
        ActionTransitions(existing.to ++ Set(new StateNeo4J(sutState, guiStateMachine)), existing.executionCounter + 1)
      } else {
        ActionTransitions(Set(new StateNeo4J(sutState, guiStateMachine)), 1)
      }
      result = result + (action -> actionTransitions)
    }
    result
  }

  private[api] override def addTransition(a: Action, to: State): Int = {
    var tx: Option[Transaction] = None
    try {
      tx = Some(guiStateMachine.graphDb.beginTx)
      val node = guiStateMachine.getNodeBySutState(sutState).get // TODO #19 What happens if the node is not found?
      val relationshipTypeAction = RelationshipTypeAction(a)
      val existingRelationships = node.getRelationships(relationshipTypeAction, Direction.OUTGOING)
      var existingRelationship: Option[Relationship] = None
      val iterator = existingRelationships.iterator()
      while (iterator.hasNext && existingRelationship.isEmpty) {
        val relationship = iterator.next()
        val sutState = guiStateMachine.getSutState(relationship.getEndNode)
        if (to.getSutState == sutState) {
          existingRelationship = Some(relationship)
        }
      }

      val counter = if (existingRelationship.isEmpty) {
        val other = guiStateMachine.getNodeBySutState(to.getSutState).get // TODO #19 What happens if the node is not found?
        val relationship = node.createRelationshipTo(other, relationshipTypeAction)
        relationship.setProperty("counter", 1)
        1
      } else {
        val r = existingRelationship.get
        val counter = r.getProperty("counter").asInstanceOf[Int] + 1
        existingRelationship.get.setProperty("counter", counter)
        counter
      }
      tx.get.success()
      counter
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }
  }

  private def getNode(): Node = {
    var tx: Option[Transaction] = None

    try {
      tx = Some(guiStateMachine.graphDb.beginTx)
      val node = guiStateMachine.getNodeBySutState(sutState).get // TODO #19 What happens if the node is not found?
      tx.get.success()
      node
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }
  }
}
