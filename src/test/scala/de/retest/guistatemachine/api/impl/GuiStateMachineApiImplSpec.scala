package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, GuiStateMachine}

class GuiStateMachineApiImplSpec extends AbstractApiSpec {

  var stateMachine: GuiStateMachine = null

  "GuiStateMachineApi" should {
    "create a new state machine" in {
      stateMachine = GuiStateMachineApiImpl.createStateMachine
      stateMachine should not be null
    }

    "remove a state machine" in {
      GuiStateMachineApiImpl.removeStateMachine(stateMachine) shouldBe true
    }
  }
}
