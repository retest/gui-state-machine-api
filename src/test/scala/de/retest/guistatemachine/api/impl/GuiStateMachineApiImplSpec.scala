package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachineApi}

class GuiStateMachineApiImplSpec extends AbstractApiSpec {
  "GuiStateMachineApi" should {
    "create, get and remove a new state machine" in {
      GuiStateMachineApi().createStateMachine("tmp")
      val stateMachine = GuiStateMachineApi().getStateMachine("tmp")
      stateMachine.isDefined shouldBe true
      val fsm = stateMachine.get
      fsm.getActionExecutionTimes.size shouldEqual 0
      fsm.getAllExploredActions.size shouldEqual 0

      GuiStateMachineApi().removeStateMachine("tmp") shouldBe true
      GuiStateMachineApi().getStateMachine("tmp").isDefined shouldBe false
    }

    "clear all state machines" in {
      GuiStateMachineApi().createStateMachine("tmp0")
      GuiStateMachineApi().createStateMachine("tmp1")
      GuiStateMachineApi().createStateMachine("tmp2")
      GuiStateMachineApi().clear()
      GuiStateMachineApi().getStateMachine("tmp0").isEmpty shouldEqual true
      GuiStateMachineApi().getStateMachine("tmp1").isEmpty shouldEqual true
      GuiStateMachineApi().getStateMachine("tmp2").isEmpty shouldEqual true
    }
  }
}
