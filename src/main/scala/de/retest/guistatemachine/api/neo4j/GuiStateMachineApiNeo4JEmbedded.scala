package de.retest.guistatemachine.api.neo4j

import java.io.File
import java.nio.file.{Path, Paths}

import com.typesafe.scalalogging.Logger
import de.retest.guistatemachine.api.{GuiStateMachine, GuiStateMachineApi}
import org.apache.commons.io.FileUtils

/**
  * This implementation is only thread-safe but cannot be used by multiple processes on the same storage directory.
  * There should be only one single instance of this implementation per storage directory shared in the whole application.
  * @param storageDirectory The directory where all subdirectories for embedded graph databases are created.
  */
class GuiStateMachineApiNeo4JEmbedded(storageDirectory: Path) extends GuiStateMachineApi {
  private val logger = Logger[GuiStateMachineApiNeo4JEmbedded]

  override def createStateMachine(name: String): GuiStateMachine = synchronized {
    if (isDirectory(name)) {
      throw new RuntimeException(s"State machine $name does already exist.")
    } else {
      val path = getPath(name)
      logger.info("Created new graph DB in {}.", path)
      val conf = EmbeddedConfig(path)
      val sessionFactory = Neo4JSessionFactory.getSessionFactory(conf)
      new GuiStateMachineNeo4J(conf, sessionFactory)
    }
  }

  override def removeStateMachine(name: String): Boolean = synchronized {
    if (isDirectory(name)) {
      val file = getFile(name)
      logger.info("Deleting state machine in {}.", file)
      FileUtils.deleteDirectory(file)
      true
    } else {
      false
    }
  }

  override def getStateMachine(name: String): Option[GuiStateMachine] = synchronized {
    if (isDirectory(name)) {
      val path = getPath(name)
      logger.info("Getting graph DB in {}.", path)
      val conf = EmbeddedConfig(path)
      val sessionFactory = Neo4JSessionFactory.getSessionFactory(conf)
      Some(new GuiStateMachineNeo4J(conf, sessionFactory))
    } else {
      None
    }
  }

  override def clear(): Unit = synchronized {
    val storageDir = storageDirectory.toFile
    if (storageDir.isDirectory) {
      storageDir.listFiles().toSeq foreach { file =>
        Neo4JSessionFactory.removeSessionFactory(EmbeddedConfig(file.toPath))
      }
      logger.info("Deleting all state machines in {}.", storageDirectory)
      FileUtils.deleteDirectory(storageDir)
    } else {
      logger.info("Directory {} does not exist.", storageDirectory)
    }
  }

  /**
    * Gets the path of the embedded database with a certain name relative to the storage directory path.
    * It needs to add an "embedded" directory since the parent directory will contain the lock and a logs directory.
    * @param name The name of the embedded database.
    * @return The path of the directory of the embedded database.
    */
  private def getPath(name: String): Path = storageDirectory.resolve(Paths.get(name, "embedded"))
  private def getFile(name: String): File = getPath(name).toFile
  private def isDirectory(name: String): Boolean = getFile(name).isDirectory
}
