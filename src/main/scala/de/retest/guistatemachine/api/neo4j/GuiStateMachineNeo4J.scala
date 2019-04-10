package de.retest.guistatemachine.api.neo4j

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State, SutStateIdentifier}
import org.neo4j.ogm.cypher.{ComparisonOperator, Filter}

import scala.collection.immutable.HashMap

class GuiStateMachineNeo4J(var uri: String) extends GuiStateMachine {
  private val logger = Logger[GuiStateMachineNeo4J]
  implicit val session = Neo4jSessionFactory.getSessionFactory(uri).openSession() // TODO #19 Close the session at some point?

  override def getState(sutStateIdentifier: SutStateIdentifier): State = {
    val result = Neo4jSessionFactory.transaction {
      getNodeBySutStateIdentifier(sutStateIdentifier) match {
        case None =>
          // Create a new node for the SUT state in the graph database.
          val sutStateEntity = new SutStateEntity(sutStateIdentifier)
          session.save(sutStateEntity)
          true

        // Do nothing if the node for the SUT state does already exist.
        case Some(_) => false
      }
    }

    if (result) {
      logger.info(s"Created new state from SUT state identifier $sutStateIdentifier.")
    }

    StateNeo4J(sutStateIdentifier, this)
  }

  override def getAllStates: Map[SutStateIdentifier, State] =
    Neo4jSessionFactory.transaction {
      val allNodes = session.loadAll(classOf[SutStateEntity])
      var result = HashMap[SutStateIdentifier, State]()
      val iterator = allNodes.iterator()

      while (iterator.hasNext) {
        val node = iterator.next()
        val sutState = new SutStateIdentifier(node.hash)
        result = result + (sutState -> StateNeo4J(sutState, this))
      }
      result
    }

  override def clear(): Unit = session.purgeDatabase()

  override def assignFrom(other: GuiStateMachine): Unit = {
    // TODO #19 Should we delete the old graph database?
    val otherStateMachine = other.asInstanceOf[GuiStateMachineNeo4J]
    uri = otherStateMachine.uri
  }

  private[neo4j] def getNodeBySutStateIdentifier(sutStateIdentifier: SutStateIdentifier): Option[SutStateEntity] = {
    val filter = new Filter("hash", ComparisonOperator.EQUALS, sutStateIdentifier.hash)
    val first = session.loadAll(classOf[SutStateEntity], filter).stream().findFirst()
    if (first.isPresent) {
      Some(first.get())
    } else {
      None
    }
  }
}
