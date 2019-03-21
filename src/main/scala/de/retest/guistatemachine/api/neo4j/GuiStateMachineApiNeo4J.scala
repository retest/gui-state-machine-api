package de.retest.guistatemachine.api.neo4j

import java.io.File

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory

import scala.collection.concurrent.TrieMap

class GuiStateMachineApiNeo4J extends GuiStateMachineApi {
  private val logger = Logger[GuiStateMachineImpl]
  private val stateMachines = TrieMap[String, GuiStateMachine]()
  // TODO #19 Load existing state machines from the disk.

  override def createStateMachine(name: String): GuiStateMachine = {
    var dir = new File(name)
    var graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dir)
    registerShutdownHook(graphDb)

    logger.info("Created new graph DB in {}.", dir.getAbsolutePath)

    val guiStateMachine = new GuiStateMachineNeo4J(graphDb)
    stateMachines += (name -> guiStateMachine)
    guiStateMachine
  }

  override def removeStateMachine(name: String): Boolean = stateMachines.remove(name).isDefined // TODO #19 Remove from disk?

  override def getStateMachine(name: String): Option[GuiStateMachine] = stateMachines.get(name)

  override def clear(): Unit = stateMachines.clear()

  private def registerShutdownHook(graphDb: GraphDatabaseService): Unit = { // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running application).
    Runtime.getRuntime.addShutdownHook(new Thread() { override def run(): Unit = { graphDb.shutdown() } })
  }
}
