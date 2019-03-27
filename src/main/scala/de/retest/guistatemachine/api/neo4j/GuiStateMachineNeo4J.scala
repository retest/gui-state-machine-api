package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.{GuiStateMachine, State}
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.Action
import org.neo4j.ogm.cypher.{ComparisonOperator, Filter}

import scala.collection.immutable.HashMap

class GuiStateMachineNeo4J(var uri: String) extends GuiStateMachine {
  implicit val session = Neo4jSessionFactory.getSessionFactory(uri).openSession() // TODO #19 Save the session at some point and close it at some point

  override def getState(sutState: SutState): State = {
    Neo4jSessionFactory.transaction {
      getNodeBySutState(sutState) match {
        case None =>
          // Create a new node for the SUT state in the graph database.
          session.save(new SutStateEntity(sutState))

        // Do nothing if the node for the SUT state does already exist.
        case _ =>
      }
    }

    StateNeo4J(sutState, this)
  }

  override def executeAction(from: State, a: Action, to: State): State = {
    from.addTransition(a, to)
    to
  }

  override def getAllStates: Map[SutState, State] =
    Neo4jSessionFactory.transaction {
      val allNodes = session.loadAll(classOf[SutStateEntity])
      var result = HashMap[SutState, State]()
      val iterator = allNodes.iterator()

      while (iterator.hasNext) {
        val node = iterator.next()
        val sutState = node.sutState
        result = result + (sutState -> StateNeo4J(sutState, this))
      }
      result
    }

  override def getAllExploredActions: Set[Action] = Set() // TODO #19 get all relationships in a transaction

  override def getActionExecutionTimes: Map[Action, Int] = Map() // TODO #19 get all execution time properties "counter" from all actions

  override def clear(): Unit =
    Neo4jSessionFactory.transaction {
      // Deletes all nodes and relationships.
      session.deleteAll(classOf[SutStateEntity])
      session.deleteAll(classOf[ActionTransitionEntity])
    }

  override def assignFrom(other: GuiStateMachine): Unit = {
    // TODO #19 Should we delete the old graph database?
    val otherStateMachine = other.asInstanceOf[GuiStateMachineNeo4J]
    uri = otherStateMachine.uri
  }

  // TODO #19 Create an index on the property "sutState": https://neo4j.com/docs/cypher-manual/current/schema/index/#schema-index-create-a-single-property-index
  // TODO #19 Should always be used inside of a transaction.
  private[neo4j] def getNodeBySutState(sutState: SutState): Option[SutStateEntity] = {
    val filter = new Filter("sutState", ComparisonOperator.EQUALS, sutState) // TODO #19 Is this how we filter for an attribute?
    val first = session.loadAll(classOf[SutStateEntity], filter).stream().findFirst()
    if (first.isPresent) {
      Some(first.get())
    } else {
      None
    }
  }
}
