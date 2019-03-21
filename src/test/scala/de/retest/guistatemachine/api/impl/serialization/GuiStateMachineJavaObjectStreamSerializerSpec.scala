package de.retest.guistatemachine.api.impl.serialization

import java.io.File

import de.retest.guistatemachine.api.impl.GuiStateMachineImpl
import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineSerializer}
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
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val finalSutState = createSutState(rootElementC)

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
      guiStateMachine.getAllExploredActions.size shouldEqual 1
      guiStateMachine.getActionExecutionTimes(action0) shouldEqual 1
      guiStateMachine.getActionExecutionTimes.contains(action1) shouldEqual false
      guiStateMachine.getAllStates.size shouldEqual 2
      val loadedInitialState = guiStateMachine.getAllStates(initialSutState)
      val loadedFinalState = guiStateMachine.getAllStates(finalSutState)
      loadedInitialState.getSutState shouldEqual initialSutState
      loadedInitialState.getTransitions.size shouldEqual 1
      loadedInitialState.getTransitions.contains(action0) shouldEqual true
      val loadedTransition = loadedInitialState.getTransitions(action0)
      loadedTransition.executionCounter shouldEqual 1
      loadedTransition.to.size shouldEqual 1
      loadedTransition.to.head shouldEqual loadedFinalState
      loadedFinalState.getSutState shouldEqual finalSutState
      loadedFinalState.getTransitions.isEmpty shouldEqual true
    }
  }
}
