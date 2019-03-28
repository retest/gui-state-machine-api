package de.retest.guistatemachine.api.neo4j

import java.io.File

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}

import scala.collection.concurrent.TrieMap

class GuiStateMachineApiNeo4J extends GuiStateMachineApi {
  private val logger = Logger[GuiStateMachineImpl]
  private val stateMachines = TrieMap[String, GuiStateMachine]()
  // TODO #19 Load existing state machines from the disk.

  override def createStateMachine(name: String): GuiStateMachine = {
    val uri = getUri(name)
    Neo4jSessionFactory.getSessionFactory(uri)
    logger.info("Created new graph DB in {}.", uri)

    val guiStateMachine = new GuiStateMachineNeo4J(uri)
    stateMachines += (name -> guiStateMachine)
    guiStateMachine
  }

  override def removeStateMachine(name: String): Boolean = stateMachines.get(name) match {
    case Some(_) =>
      if (stateMachines.remove(name).isDefined) {
        val uri = getUri(name)
        Neo4jSessionFactory.getSessionFactory(uri).close() // TODO #19 Removes from disk?
        true
      } else {
        false
      }
    case None => false
  }

  override def getStateMachine(name: String): Option[GuiStateMachine] = stateMachines.get(name)

  override def clear(): Unit = stateMachines.keySet foreach { name => // TODO #19 keys can be modified concurrently. So we might not remove all state machines?
    removeStateMachine(name)
  } // TODO #19 Removes from disk?

  private def getUri(name: String): String = new File(name).toURI.toString
}
