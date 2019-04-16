package de.retest.guistatemachine.api.neo4j

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, State, SutStateIdentifier}
import org.neo4j.ogm.cypher.{ComparisonOperator, Filter}
import org.neo4j.ogm.session.Session

import scala.collection.immutable.HashMap

class GuiStateMachineNeo4J(var uri: String) extends GuiStateMachine {
  private val logger = Logger[GuiStateMachineNeo4J]

  override def getState(sutStateIdentifier: SutStateIdentifier): State = {
    val result = Neo4jSessionFactory.transaction { session =>
      getNodeBySutStateIdentifier(session, sutStateIdentifier) match {
        case None =>
          // Create a new node for the SUT state in the graph database.
          val sutStateEntity = new SutStateEntity(sutStateIdentifier)
          session.save(sutStateEntity)
          true

        // Do nothing if the node for the SUT state does already exist.
        case Some(_) => false
      }
    }(uri)

    if (result) {
      logger.info(s"Created new state from SUT state identifier $sutStateIdentifier.")
    }

    StateNeo4J(sutStateIdentifier, this)
  }

  override def getAllStates: Map[SutStateIdentifier, State] =
    Neo4jSessionFactory.transaction { session =>
      val allNodes = session.loadAll(classOf[SutStateEntity])
      var result = HashMap[SutStateIdentifier, State]()
      val iterator = allNodes.iterator()

      while (iterator.hasNext) {
        val node = iterator.next()
        val sutState = new SutStateIdentifier(node.hash, node.message)
        result = result + (sutState -> StateNeo4J(sutState, this))
      }
      result
    }(uri)

  override def clear(): Unit =
    Neo4jSessionFactory.transaction { session =>
      session.purgeDatabase()
    }(uri)

  override def assignFrom(other: GuiStateMachine): Unit = {
    // TODO #19 Should we delete the old graph database?
    val otherStateMachine = other.asInstanceOf[GuiStateMachineNeo4J]
    uri = otherStateMachine.uri
  }

  private[neo4j] def getNodeBySutStateIdentifier(session: Session, sutStateIdentifier: SutStateIdentifier): Option[SutStateEntity] = {
    val filter = new Filter(SutStateEntity.PropertyNameHash, ComparisonOperator.EQUALS, sutStateIdentifier.hash)
    val first = session.loadAll(classOf[SutStateEntity], filter).stream().findFirst()
    if (first.isPresent) {
      Some(first.get())
    } else {
      None
    }
  }

  private[neo4j] def getNodeBySutStateIdentifierOrThrow(session: Session, sutStateIdentifier: SutStateIdentifier): SutStateEntity =
    getNodeBySutStateIdentifier(session, sutStateIdentifier) match {
      case Some(sutStateEntity) => sutStateEntity
      case None                 => throw new RuntimeException(s"Missing state $sutStateIdentifier")
    }
}
