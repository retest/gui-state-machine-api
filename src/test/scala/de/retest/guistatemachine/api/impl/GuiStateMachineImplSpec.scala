package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

class GuiStateMachineImplSpec extends AbstractApiSpec {

  val sut = new GuiStateMachineImpl
  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val action0Mock = getAction()
  val action1Mock = getAction()

  "GuiStateMachine" should {
    "get an initial state" in {
      val initial = sut.getState(getDescriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 2
      initial.getTransitions.size shouldEqual 0
    }

    "add two transitions to two new states for the same action" in {
      val initialDescriptors = getDescriptors
      val initial = sut.getState(getDescriptors, getNeverExploredActions)

      val s0Descriptors = Descriptors(Set(rootElementA))
      // It should be a new state and not the same again.
      initialDescriptors.equals(s0Descriptors) shouldEqual false
      val s0 = sut.executeAction(initial, Action(action0Mock), s0Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(Action(action0Mock)).size shouldEqual 1
      s0.getNeverExploredActions.size shouldEqual 2
      s0.getTransitions.size shouldEqual 0

      val s1Descriptors = Descriptors(Set(rootElementB))
      // It should be a new state and not the same again.
      initialDescriptors.equals(s1Descriptors) shouldEqual false
      val s1 = sut.executeAction(initial, Action(action0Mock), s1Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(Action(action0Mock)).size shouldEqual 2
      s1.getNeverExploredActions.size shouldEqual 2
      s1.getTransitions.size shouldEqual 0
    }
  }

  def getDescriptors: Descriptors = Descriptors(Set(rootElementA, rootElementB))
  def getNeverExploredActions: Set[Action] = Set(Action(action0Mock), Action(action1Mock))
}
