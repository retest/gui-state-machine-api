package de.retest.guistatemachine.api.impl

import java.io.File

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors, Id}

class GuiStateMachineApiImplSpec extends AbstractApiSpec {

  var stateMachineId = Id(-1)

  "GuiStateMachineApi" should {
    "create a new state machine" in {
      stateMachineId = GuiStateMachineApiImpl.createStateMachine
      stateMachineId shouldEqual Id(0)
    }

    "get a state machine" in {
      val stateMachine = GuiStateMachineApiImpl.getStateMachine(stateMachineId)
      stateMachine.isDefined shouldBe true
      val fsm = stateMachine.get
      fsm.getActionExecutionTimes.size shouldEqual 0
      fsm.getAllExploredActions.size shouldEqual 0
      fsm.getAllNeverExploredActions.size shouldEqual 0
    }

    "remove a state machine" in {
      GuiStateMachineApiImpl.removeStateMachine(stateMachineId) shouldBe true
    }

    "clear all state machines" in {
      GuiStateMachineApiImpl.createStateMachine shouldEqual Id(0)
      GuiStateMachineApiImpl.createStateMachine shouldEqual Id(1)
      GuiStateMachineApiImpl.createStateMachine shouldEqual Id(2)
      GuiStateMachineApiImpl.clear()
      GuiStateMachineApiImpl.getStateMachine(Id(2)).isEmpty shouldEqual true
    }

    "save and load" in {
      val filePath = "./test_state_machines"
      val oldFile = new File(filePath)

      if (oldFile.exists()) oldFile.delete() shouldEqual true

      val rootElementA = getRootElement("a")
      val rootElementB = getRootElement("b")
      val rootElementC = getRootElement("c")
      val action0 = Action(Id(0))
      val action1 = Action(Id(1))

      val initialDescriptors = Descriptors(Set(rootElementA, rootElementB, rootElementC))
      val initialNeverExploredActions = Set(action0, action1)
      val finalDescriptors = Descriptors(Set(rootElementC))
      val finalNeverExploredActions = Set(action0, action1)

      // Create the whole state machine:
      GuiStateMachineApiImpl.clear
      stateMachineId = GuiStateMachineApiImpl.createStateMachine
      val stateMachine = GuiStateMachineApiImpl.getStateMachine(stateMachineId).get
      val initialState = stateMachine.getState(initialDescriptors, initialNeverExploredActions)
      val finalState = stateMachine.executeAction(initialState, action0, finalDescriptors, finalNeverExploredActions)

      // Save all state machines:
      GuiStateMachineApiImpl.save(filePath)
      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      // Load all state machines:
      GuiStateMachineApiImpl.clear
      GuiStateMachineApiImpl.load(filePath)

      // Verify all loaded state machines:
      val loadedStateMachineOp = GuiStateMachineApiImpl.getStateMachine(stateMachineId)
      loadedStateMachineOp.isDefined shouldEqual true
      val loadedStateMachine = loadedStateMachineOp.get.asInstanceOf[GuiStateMachineImpl]

      loadedStateMachine.getAllExploredActions.size shouldEqual 1
      loadedStateMachine.getAllNeverExploredActions.size shouldEqual 1
      loadedStateMachine.getActionExecutionTimes(action0) shouldEqual 1
      loadedStateMachine.getActionExecutionTimes.contains(action1) shouldEqual false
      loadedStateMachine.states.size shouldEqual 2
      val loadedInitialState = loadedStateMachine.states(initialDescriptors)
      val loadedFinalState = loadedStateMachine.states(finalDescriptors)
      loadedInitialState.getDescriptors shouldEqual initialDescriptors
      loadedInitialState.getTransitions.size shouldEqual 1
      loadedInitialState.getTransitions.contains(action0) shouldEqual true
      loadedInitialState.getNeverExploredActions.size shouldEqual 1
      val loadedTransition = loadedInitialState.getTransitions(action0)
      loadedTransition.executionCounter shouldEqual 1
      loadedTransition.to.size shouldEqual 1
      loadedTransition.to.toList(0) shouldEqual loadedFinalState
      loadedFinalState.getDescriptors shouldEqual finalDescriptors
      loadedFinalState.getTransitions.isEmpty shouldEqual true
      loadedFinalState.getNeverExploredActions shouldEqual finalNeverExploredActions
    }
  }
}
