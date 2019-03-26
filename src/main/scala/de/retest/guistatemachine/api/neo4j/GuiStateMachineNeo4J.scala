package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.{GuiStateMachine, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.graphdb.{GraphDatabaseService, Node, ResourceIterator, Transaction}

import scala.collection.immutable.HashMap

class GuiStateMachineNeo4J(var graphDb: GraphDatabaseService) extends GuiStateMachine {

  override def getState(sutState: SutState): State = {
    var tx: Option[Transaction] = None
    try {
      tx = Some(graphDb.beginTx)
      getNodeBySutState(sutState) match {
        case None => {
          // Create a new node for the sutState in the graph database.
          val node = graphDb.createNode
          node.addLabel(SutStateLabel)
          // TODO #19 SutState is not a supported property value!
          val value = new SutStateConverter().toGraphProperty(sutState)
          node.setProperty("sutState", value)
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
    try {
      tx = Some(graphDb.beginTx)
      val allNodes = graphDb.getAllNodes()
      var result = HashMap[SutState, State]()
      val iterator = allNodes.iterator()

      while (iterator.hasNext) {
        val node = iterator.next()
        val sutState = getSutState(node)
        result = result + (sutState -> new StateNeo4J(sutState, this))
      }
      tx.get.success()
      result
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }
  }

  override def getAllExploredActions: Set[Action] = Set() // TODO #19 get all relationships in a transaction

  override def getActionExecutionTimes: Map[Action, Int] = Map() // TODO #19 get all execution time properties "counter" from all actions

  override def clear(): Unit = {
    var tx: Option[Transaction] = None
    try {
      tx = Some(graphDb.beginTx)
      // Deletes all nodes and relationships.
      graphDb.execute("MATCH (n)\nDETACH DELETE n")
      tx.get.success
    } finally {
      if (tx.isDefined) { tx.get.close() }
    }
  }

  override def assignFrom(other: GuiStateMachine): Unit = {
    clear()
    val otherStateMachine = other.asInstanceOf[GuiStateMachineNeo4J]
    graphDb = otherStateMachine.graphDb
  }

  private[neo4j] def getSutState(node: Node): SutState = {
    val value = node.getProperty("sutState").asInstanceOf[String]
    new SutStateConverter().toEntityAttribute(value)
  }

  // TODO #19 Create an index on the property "sutState": https://neo4j.com/docs/cypher-manual/current/schema/index/#schema-index-create-a-single-property-index
  private[neo4j] def getNodeBySutState(sutState: SutState): Option[Node] = {
    var nodes: Option[ResourceIterator[Node]] = None
    val first = try {
      val value = new SutStateConverter().toGraphProperty(sutState)
      nodes = Some(graphDb.findNodes(SutStateLabel, "sutState", value))
      nodes.get.stream().findFirst()
    } finally {
      if (nodes.isDefined) {
        nodes.get.close()
      }
    }
    if (first.isPresent) {
      Some(first.get())
    } else {
      None
    }
  }
}
