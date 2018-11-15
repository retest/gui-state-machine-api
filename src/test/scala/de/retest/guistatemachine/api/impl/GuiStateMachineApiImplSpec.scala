package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Id}

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
  }
}
