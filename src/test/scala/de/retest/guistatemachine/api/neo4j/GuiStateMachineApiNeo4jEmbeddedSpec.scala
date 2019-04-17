package de.retest.guistatemachine.api.neo4j

import java.nio.file.Files

import de.retest.guistatemachine.api.{AbstractGuiStateMachineApiSpec, GuiStateMachineApi}
import org.scalatest.BeforeAndAfterAll

class GuiStateMachineApiNeo4jEmbeddedSpec extends AbstractGuiStateMachineApiSpec with BeforeAndAfterAll {
  private val tempDir = Files.createTempDirectory("GuiStateMachineApiNeo4jSpec").toFile
  override def getName: String = "GuiStateMachineApiNeo4J"
  override def getCut: GuiStateMachineApi = new GuiStateMachineApiNeo4JEmbedded(tempDir.toPath)

  override def afterAll(): Unit = {
    tempDir.delete()
  }
}
