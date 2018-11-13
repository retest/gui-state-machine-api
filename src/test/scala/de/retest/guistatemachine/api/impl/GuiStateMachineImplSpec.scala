package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

class GuiStateMachineImplSpec extends AbstractApiSpec {

  val sut = new GuiStateMachineImpl
  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val rootElementC = getRootElement("c")
  val action0Mock = getAction()
  val action1Mock = getAction()

  "GuiStateMachine" should {
    "add two transitions to two new states for the same action and one transition to another state for another action" in {
      val initialDescriptors = getDescriptors
      val initial = sut.getState(getDescriptors, getNeverExploredActions)
      sut.getAllExploredActions.size shouldEqual 0
      sut.getAllNeverExploredActions.size shouldEqual 2

      // execute action0Mock for the first time
      val s0Descriptors = Descriptors(Set(rootElementA))
      val s0 = sut.executeAction(initial, Action(action0Mock), s0Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(Action(action0Mock)).size shouldEqual 1
      s0.getNeverExploredActions.size shouldEqual 2
      s0.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getAllNeverExploredActions.size shouldEqual 1

      // execute action0Mock for the second time
      val s1Descriptors = Descriptors(Set(rootElementB))
      val s1 = sut.executeAction(initial, Action(action0Mock), s1Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(Action(action0Mock)).size shouldEqual 2
      s1.getNeverExploredActions.size shouldEqual 2
      s1.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getAllNeverExploredActions.size shouldEqual 1

      // execute action1Mock for the first time
      val s2Descriptors = Descriptors(Set(rootElementC))
      val s2 = sut.executeAction(initial, Action(action1Mock), s2Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 0
      initial.getTransitions.size shouldEqual 2
      initial.getTransitions(Action(action1Mock)).size shouldEqual 1
      s2.getNeverExploredActions.size shouldEqual 2
      s2.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 2
      sut.getAllNeverExploredActions.size shouldEqual 0
    }

    "store a state for the second access" in {
      val initialDescriptors = getDescriptors
      val initialFromAccess0 = sut.getState(getDescriptors, getNeverExploredActions)
      val initialFromAccess1 = sut.getState(getDescriptors, getNeverExploredActions)
      initialFromAccess0 shouldEqual initialFromAccess1
    }
  }

  def getDescriptors: Descriptors = Descriptors(Set(rootElementA, rootElementB, rootElementC))
  def getNeverExploredActions: Set[Action] = Set(Action(action0Mock), Action(action1Mock))
}