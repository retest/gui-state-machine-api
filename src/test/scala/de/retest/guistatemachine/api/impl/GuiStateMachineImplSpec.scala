package de.retest.guistatemachine.api.impl

import java.util.Arrays

import de.retest.guistatemachine.api.{AbstractApiSpec, Descriptors}
import de.retest.surili.model.{Action, NavigateToAction}
import de.retest.ui.descriptors.SutState

class GuiStateMachineImplSpec extends AbstractApiSpec {
  val sut = new GuiStateMachineImpl
  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val rootElementC = getRootElement("c")
  val action0 = new NavigateToAction("http://google.com")
  val action1 = new NavigateToAction("http://wikipedia.org")

  "GuiStateMachine" should {
    "add two transitions to two new states for the same action and one transition to another state for another action" in {
      val initialDescriptors = getDescriptors
      val initial = sut.getState(initialDescriptors, getNeverExploredActions)
      sut.getAllExploredActions.size shouldEqual 0
      sut.getAllNeverExploredActions.size shouldEqual 2
      sut.getActionExecutionTimes.size shouldEqual 0

      // execute action0 for the first time
      val s0Descriptors = Descriptors(new SutState(Arrays.asList(rootElementA)))
      val s0 = sut.executeAction(initial, action0, s0Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(action0).to.size shouldEqual 1
      initial.getTransitions(action0).executionCounter shouldEqual 1
      s0.getNeverExploredActions.size shouldEqual 2
      s0.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getAllNeverExploredActions.size shouldEqual 1
      sut.getActionExecutionTimes.get(action0).isDefined shouldEqual true
      sut.getActionExecutionTimes.get(action0).get shouldEqual 1

      // execute action0 for the second time
      val s1Descriptors = Descriptors(new SutState(Arrays.asList((rootElementB))))
      val s1 = sut.executeAction(initial, action0, s1Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 1
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(action0).to.size shouldEqual 2
      initial.getTransitions(action0).executionCounter shouldEqual 2
      s1.getNeverExploredActions.size shouldEqual 2
      s1.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getAllNeverExploredActions.size shouldEqual 1
      sut.getActionExecutionTimes.get(action0).isDefined shouldEqual true
      sut.getActionExecutionTimes.get(action0).get shouldEqual 2

      // execute action1 for the first time
      val s2Descriptors = Descriptors(new SutState(Arrays.asList(rootElementC)))
      val s2 = sut.executeAction(initial, action1, s2Descriptors, getNeverExploredActions)
      initial.getNeverExploredActions.size shouldEqual 0
      initial.getTransitions.size shouldEqual 2
      initial.getTransitions(action1).to.size shouldEqual 1
      initial.getTransitions(action1).executionCounter shouldEqual 1
      s2.getNeverExploredActions.size shouldEqual 2
      s2.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 2
      sut.getAllNeverExploredActions.size shouldEqual 0
      sut.getActionExecutionTimes.get(action1).isDefined shouldEqual true
      sut.getActionExecutionTimes.get(action1).get shouldEqual 1
    }

    "store a state for the second access" in {
      val initialDescriptors = getDescriptors
      val initialFromAccess0 = sut.getState(initialDescriptors, getNeverExploredActions)
      val initialFromAccess1 = sut.getState(initialDescriptors, getNeverExploredActions)
      initialFromAccess0 shouldEqual initialFromAccess1
    }

    "clear the state machine" in {
      sut.clear()
      sut.getAllNeverExploredActions.isEmpty shouldEqual true
      sut.getAllExploredActions.isEmpty shouldEqual true
      sut.actionExecutionTimes.isEmpty shouldEqual true
      sut.states.isEmpty shouldEqual true
    }
  }

  def getDescriptors = Descriptors(new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC)))
  def getNeverExploredActions = Set[Action](action0, action1)
}
