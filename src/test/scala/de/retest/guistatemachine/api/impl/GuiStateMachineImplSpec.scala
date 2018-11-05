package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

class GuiStateMachineImplSpec extends AbstractApiSpec {

  val rootElement0Mock = getRootElement()
  val rootElement1Mock = getRootElement()
  val action0Mock = getAction()
  val action1Mock = getAction()

  "GuiStateMachine" should {
    "get an initial state" in {
      val initial = GuiStateMachineImpl.getState(getDescriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 2
      initial.getTransitions.size shouldEqual 0
    }

    "add a transition" in {
      val initial = GuiStateMachineImpl.getState(getDescriptors, getNeverExploredActions)
      val s = GuiStateMachineImpl.executeAction(initial, Action(action0Mock), Descriptors(Set(rootElement0Mock)), getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      s.getNeverExploredActions.size shouldEqual 2
      s.getTransitions.size shouldEqual 0
    }
  }

  def getDescriptors: Descriptors = Descriptors(Set(rootElement0Mock, rootElement1Mock))
  def getNeverExploredActions: Set[Action] = Set(Action(action0Mock), Action(action1Mock))
}
