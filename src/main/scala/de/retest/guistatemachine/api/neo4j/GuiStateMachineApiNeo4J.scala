package de.retest.guistatemachine.api.neo4j

import java.io.File
import java.nio.file.Paths

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}
import org.apache.commons.io.FileUtils

class GuiStateMachineApiNeo4J(directory: String) extends GuiStateMachineApi {
  private val logger = Logger[GuiStateMachineApiNeo4J]

  override def createStateMachine(name: String): GuiStateMachine =
    if (isDirectory(name)) {
      throw new RuntimeException(s"State machine $name does already exist.")
    } else {
      val uri = getUri(name)
      Neo4jSessionFactory.getSessionFactoryEmbedded(uri)
      logger.info("Created new graph DB in {}.", uri)

      new GuiStateMachineNeo4J(uri)
    }

  override def removeStateMachine(name: String): Boolean =
    if (isDirectory(name)) {
      val file = getFile(name)
      logger.info("Deleting state machine in {}.", file)
      FileUtils.deleteDirectory(file)
      true
    } else {
      false
    }

  override def getStateMachine(name: String): Option[GuiStateMachine] =
    if (isDirectory(name)) {
      val uri = getUri(name)
      Some(new GuiStateMachineNeo4J(uri))
    } else {
      None
    }

  override def clear(): Unit = {
    logger.info("Deleting all state machines in {}.", directory)
    FileUtils.deleteDirectory(getStorageDirectory())
  }

  private def getStorageDirectory(): File = new File(directory)
  private def isDirectory(name: String): Boolean = getFile(name).isDirectory
  private def getFile(name: String): File = new File(Paths.get(directory, name).toAbsolutePath.toString)
  private def getUri(name: String): String = getFile(name).toURI.toString
}
