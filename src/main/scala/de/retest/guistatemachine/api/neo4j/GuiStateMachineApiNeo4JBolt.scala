package de.retest.guistatemachine.api.neo4j

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}

class GuiStateMachineApiNeo4JBolt(url: String, port: Int, user: String, password: String) extends GuiStateMachineApi {
  private val logger = Logger[GuiStateMachineApiNeo4JBolt]

  override def createStateMachine(name: String): GuiStateMachine = {
    val conf = BoltConfig(url, port, user, password) // TODO #19 How to distinguish between databases.
    new GuiStateMachineNeo4J(conf, Neo4JSessionFactory.getSessionFactory(conf))
  }

  override def removeStateMachine(name: String): Boolean = false // TODO #19 How to remove a Bolt database?

  override def getStateMachine(name: String): Option[GuiStateMachine] = None // TODO #19 How to get a Bolt database? Some list?

  override def clear(): Unit = {} // TODO #19 How to remove all Bolt databases?
}
