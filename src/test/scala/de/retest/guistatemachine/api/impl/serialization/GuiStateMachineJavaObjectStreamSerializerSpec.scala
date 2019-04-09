package de.retest.guistatemachine.api.impl.serialization

import java.io.File

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{AbstractApiSpec, ActionIdentifier, GuiStateMachineSerializer, SutStateIdentifier}
import de.retest.surili.commons.actions.NavigateToAction
import org.scalatest.BeforeAndAfterEach

class GuiStateMachineJavaObjectStreamSerializerSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val guiStateMachine = new GuiStateMachineImpl

  override def beforeEach() {
    guiStateMachine.clear()
  }

  "GuiStateMachineJavaObjectStreamSerializer" should {
    "save and load" in {
      val filePath = "./target/test_state_machine"
      val oldFile = new File(filePath)

      if (oldFile.exists()) { oldFile.delete() } shouldEqual true

      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action0Identifier = new ActionIdentifier(action0)

      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val initialSutStateIdentifier = new SutStateIdentifier(initialSutState)
      val finalSutState = createSutState(rootElementC)
      val finalSutStateIdentifier = new SutStateIdentifier(finalSutState)

      // Create the whole state machine:
      guiStateMachine.executeAction(initialSutState, action0, finalSutState)

      // Save the state machine:
      GuiStateMachineSerializer.javaObjectStream(guiStateMachine).save(filePath)
      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      // Load the state machine:
      guiStateMachine.clear()
      GuiStateMachineSerializer.javaObjectStream(guiStateMachine).load(filePath)

      // Verify the loaded state machine:
      guiStateMachine.getAllStates.size shouldEqual 2
      val loadedInitialState = guiStateMachine.getAllStates(initialSutStateIdentifier)
      val loadedFinalState = guiStateMachine.getAllStates(finalSutStateIdentifier)
      loadedInitialState.getSutStateIdentifier shouldEqual initialSutStateIdentifier
      loadedInitialState.getOutgoingActionTransitions.size shouldEqual 1
      loadedInitialState.getOutgoingActionTransitions.contains(action0Identifier) shouldEqual true
      val loadedTransition = loadedInitialState.getOutgoingActionTransitions(action0Identifier)
      loadedTransition.executionCounter shouldEqual 1
      loadedTransition.states.size shouldEqual 1
      loadedTransition.states.head shouldEqual loadedFinalState
      loadedFinalState.getSutStateIdentifier shouldEqual finalSutStateIdentifier
      loadedFinalState.getOutgoingActionTransitions.isEmpty shouldEqual true
    }
  }
}
