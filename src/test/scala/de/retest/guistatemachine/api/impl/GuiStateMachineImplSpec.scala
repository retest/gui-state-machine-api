package de.retest.guistatemachine.api.impl

import java.io.File
import java.util.Arrays

import de.retest.guistatemachine.api.AbstractApiSpec
import de.retest.surili.model.actions.{Action, NavigateToAction}
import de.retest.ui.descriptors.SutState

class GuiStateMachineImplSpec extends AbstractApiSpec {
  private val sut = new GuiStateMachineImpl
  private val rootElementA = getRootElement("a")
  private val rootElementB = getRootElement("b")
  private val rootElementC = getRootElement("c")
  private val action0 = new NavigateToAction("http://google.com")
  private val action1 = new NavigateToAction("http://wikipedia.org")

  "GuiStateMachine" should {
    "add two transitions to two new states for the same action and one transition to another state for another action" in {
      val initialSutState = getSutState
      val initial = sut.getState(initialSutState, getNeverExploredActions)
      sut.getAllExploredActions.size shouldEqual 0
      sut.getAllNeverExploredActions.size shouldEqual 2
      sut.getActionExecutionTimes.size shouldEqual 0

      // execute action0 for the first time
      val s0SutState = new SutState(Arrays.asList(rootElementA))
      val s0 = sut.getState(s0SutState, getNeverExploredActions)
      sut.executeAction(initial, action0, s0)
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
      val s1SutState = new SutState(Arrays.asList(rootElementB))
      val s1 = sut.getState(s1SutState, getNeverExploredActions)
      sut.executeAction(initial, action0, s1)
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
      val s2SutState = new SutState(Arrays.asList(rootElementC))
      val s2 = sut.getState(s2SutState, getNeverExploredActions)
      sut.executeAction(initial, action1, s2)
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
      val initialSutState = getSutState
      val initialFromAccess0 = sut.getState(initialSutState, getNeverExploredActions)
      val initialFromAccess1 = sut.getState(initialSutState, getNeverExploredActions)
      initialFromAccess0 shouldEqual initialFromAccess1
    }

    "clear the state machine" in {
      sut.clear()
      sut.getAllNeverExploredActions.isEmpty shouldEqual true
      sut.getAllExploredActions.isEmpty shouldEqual true
      sut.actionExecutionTimes.isEmpty shouldEqual true
      sut.states.isEmpty shouldEqual true
    }

    "save GML " in {
      val rootElementA = getRootElement("a")
      val rootElementB = getRootElement("b")
      val rootElementC = getRootElement("c")
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
      val initialNeverExploredActions = Set[Action](action0, action1)
      val finalSutState = new SutState(Arrays.asList(rootElementC))
      val finalNeverExploredActions = Set[Action](action0, action1)

      // Create the whole state machine:
      sut.clear()
      val initialState = sut.getState(initialSutState, initialNeverExploredActions)
      val finalState = sut.getState(finalSutState, finalNeverExploredActions)
      sut.executeAction(initialState, action0, finalState)
      sut.executeAction(initialState, action1, finalState)
      sut.executeAction(finalState, action0, initialState)
      sut.executeAction(finalState, action1, initialState)

      initialState.getNeverExploredActions.size shouldEqual 0
      initialState.getTransitions.size shouldEqual 2
      finalState.getNeverExploredActions.size shouldEqual 0
      finalState.getTransitions.size shouldEqual 2

      val filePath = "./target/test_state_machine.gml"
      val oldFile = new File(filePath)

      if (oldFile.exists()) oldFile.delete() shouldEqual true

      sut.saveGML(filePath)

      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false
    }
  }

  def getSutState: SutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
  def getNeverExploredActions: Set[Action] = Set[Action](action0, action1)
}
