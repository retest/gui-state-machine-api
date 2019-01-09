package de.retest.guistatemachine.api.impl

import java.io.File
import java.util.Arrays

import de.retest.guistatemachine.api.{AbstractApiSpec, Id}
import de.retest.surili.model.actions.{Action, NavigateToAction}
import de.retest.ui.descriptors.SutState

class GuiStateMachineApiImplSpec extends AbstractApiSpec {
  val sut = new GuiStateMachineApiImpl
  var stateMachineId = Id(-1)

  "GuiStateMachineApi" should {
    "create, get and remove a new state machine" in {
      stateMachineId = sut.createStateMachine()
      stateMachineId shouldEqual Id(0)

      val stateMachine = sut.getStateMachine(stateMachineId)
      stateMachine.isDefined shouldBe true
      val fsm = stateMachine.get
      fsm.getActionExecutionTimes.size shouldEqual 0
      fsm.getAllExploredActions.size shouldEqual 0
      fsm.getAllNeverExploredActions.size shouldEqual 0

      sut.removeStateMachine(stateMachineId) shouldBe true
    }

    "clear all state machines" in {
      sut.createStateMachine shouldEqual Id(0)
      sut.createStateMachine shouldEqual Id(1)
      sut.createStateMachine shouldEqual Id(2)
      sut.clear()
      sut.getStateMachine(Id(2)).isEmpty shouldEqual true
    }

    "save and load" in {
      val filePath = "./target/test_state_machines"
      val oldFile = new File(filePath)

      if (oldFile.exists()) oldFile.delete() shouldEqual true

      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
      val initialNeverExploredActions = Set[Action](action0, action1)
      val finalSutState = new SutState(Arrays.asList(rootElementC))
      val finalNeverExploredActions = Set[Action](action0, action1)

      // Create the whole state machine:
      sut.clear()
      stateMachineId = sut.createStateMachine()
      val stateMachine = sut.getStateMachine(stateMachineId).get
      val initialState = stateMachine.getState(initialSutState, initialNeverExploredActions)
      val finalState = stateMachine.getState(finalSutState, finalNeverExploredActions)
      stateMachine.executeAction(initialState, action0, finalState)

      // Save all state machines:
      sut.save(filePath)
      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      // Load all state machines:
      sut.clear()
      sut.load(filePath)

      // Verify all loaded state machines:
      val loadedStateMachineOp = sut.getStateMachine(stateMachineId)
      loadedStateMachineOp.isDefined shouldEqual true
      val loadedStateMachine = loadedStateMachineOp.get.asInstanceOf[GuiStateMachineImpl]

      loadedStateMachine.getAllExploredActions.size shouldEqual 1
      loadedStateMachine.getAllNeverExploredActions.size shouldEqual 1
      loadedStateMachine.getActionExecutionTimes(action0) shouldEqual 1
      loadedStateMachine.getActionExecutionTimes.contains(action1) shouldEqual false
      loadedStateMachine.getAllStates.size shouldEqual 2
      val loadedInitialState = loadedStateMachine.getAllStates(initialSutState)
      val loadedFinalState = loadedStateMachine.getAllStates(finalSutState)
      loadedInitialState.getSutState shouldEqual initialSutState
      loadedInitialState.getTransitions.size shouldEqual 1
      loadedInitialState.getTransitions.contains(action0) shouldEqual true
      loadedInitialState.getNeverExploredActions.size shouldEqual 1
      val loadedTransition = loadedInitialState.getTransitions(action0)
      loadedTransition.executionCounter shouldEqual 1
      loadedTransition.to.size shouldEqual 1
      loadedTransition.to.head shouldEqual loadedFinalState
      loadedFinalState.getSutState shouldEqual finalSutState
      loadedFinalState.getTransitions.isEmpty shouldEqual true
      loadedFinalState.getNeverExploredActions shouldEqual finalNeverExploredActions
    }
  }
}
