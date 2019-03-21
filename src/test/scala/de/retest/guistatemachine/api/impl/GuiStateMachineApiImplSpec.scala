package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineApi, Id}

class GuiStateMachineApiImplSpec extends AbstractApiSpec {
  "GuiStateMachineApi" should {
    "create, get and remove a new state machine" in {
      val stateMachineId = GuiStateMachineApi().createStateMachine()
      stateMachineId shouldEqual Id(0)

      val stateMachine = GuiStateMachineApi().getStateMachine(stateMachineId)
      stateMachine.isDefined shouldBe true
      val fsm = stateMachine.get
      fsm.getActionExecutionTimes.size shouldEqual 0
      fsm.getAllExploredActions.size shouldEqual 0

      GuiStateMachineApi().removeStateMachine(stateMachineId) shouldBe true
    }

    "clear all state machines" in {
      GuiStateMachineApi().createStateMachine shouldEqual Id(0)
      GuiStateMachineApi().createStateMachine shouldEqual Id(1)
      GuiStateMachineApi().createStateMachine shouldEqual Id(2)
      GuiStateMachineApi().clear()
      GuiStateMachineApi().getStateMachine(Id(2)).isEmpty shouldEqual true
    }
  }
}
