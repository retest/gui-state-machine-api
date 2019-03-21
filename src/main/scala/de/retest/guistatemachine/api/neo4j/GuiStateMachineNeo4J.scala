package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.{GuiStateMachine, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.graphdb.{GraphDatabaseService, Node, Transaction}

import scala.collection.immutable.HashMap

class GuiStateMachineNeo4J(var graphDb: GraphDatabaseService) extends GuiStateMachine {

  override def getState(sutState: SutState): State = {
    var tx: Option[Transaction] = None
    try {
      tx = Some(graphDb.beginTx)
      getNodeBySutState(sutState) match {
        case None => {
          val node = graphDb.createNode
          // TODO #19 SutState is not a supported property value!
          node.setProperty("sutState", sutState)
        }
        case _ =>
      }
      tx.get.success
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }

    new StateNeo4J(sutState, this)
  }

  override def executeAction(from: State, a: Action, to: State): State = {
    from.addTransition(a, to)
    to
  }

  override def getAllStates: Map[SutState, State] = {
    var tx: Option[Transaction] = None
    val allNodes = try {
      tx = Some(graphDb.beginTx)
      val allNodes = graphDb.getAllNodes()
      tx.get.success()
      allNodes
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }

    var result = HashMap[SutState, State]()
    val iterator = allNodes.iterator()

    while (iterator.hasNext) {
      val node = iterator.next()
      val sutState = node.getProperty("sutState").asInstanceOf[SutState]
      result = result + (sutState -> new StateNeo4J(sutState, this))
    }
    result
  }

  override def getAllExploredActions: Set[Action] = Set() // TODO #19 get all relationships in a transaction

  override def getActionExecutionTimes: Map[Action, Int] = Map() // TODO #19 get all execution time properties "counter" from all actions

  override def clear(): Unit = {
    var tx: Transaction = null
    try {
      tx = graphDb.beginTx
      // Deletes all nodes and relationships.
      graphDb.execute("MATCH (n)\nDETACH DELETE n")
      tx.success
    } finally {
      if (tx != null) { tx.close() }
    }
  }

  override def assignFrom(other: GuiStateMachine): Unit = {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineNeo4J]
    graphDb = otherStateMachine.graphDb
  }

  // TODO #19 Create an index on the property "sutState": https://neo4j.com/docs/cypher-manual/current/schema/index/#schema-index-create-a-single-property-index
  private[neo4j] def getNodeBySutState(sutState: SutState): Option[Node] = {
    val nodes = graphDb.findNodes(SutStateLabel, "sutState", sutState)
    val first = nodes.stream().findFirst()
    if (first.isPresent) {
      Some(first.get())
    } else {
      None
    }
  }
}
