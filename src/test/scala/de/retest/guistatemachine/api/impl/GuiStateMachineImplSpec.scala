package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

class GuiStateMachineImplSpec extends AbstractApiSpec {

  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val action0Mock = getAction()
  val action1Mock = getAction()

  "GuiStateMachine" should {
    "get an initial state" in {
      val initial = GuiStateMachineImpl.getState(getDescriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 2
      initial.getTransitions.size shouldEqual 0
    }

    "add a transition to a new state" in {
      val initialDescriptors = getDescriptors
      val initial = GuiStateMachineImpl.getState(getDescriptors, getNeverExploredActions)
      val sDescriptors = Descriptors(Set(rootElementA))
      // It should be a new state and not the same again.
      initialDescriptors.equals(sDescriptors) shouldEqual false
      val s = GuiStateMachineImpl.executeAction(initial, Action(action0Mock), sDescriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      s.getNeverExploredActions.size shouldEqual 2
      s.getTransitions.size shouldEqual 0
    }
  }

  def getDescriptors: Descriptors = Descriptors(Set(rootElementA, rootElementB))
  def getNeverExploredActions: Set[Action] = Set(Action(action0Mock), Action(action1Mock))
}
