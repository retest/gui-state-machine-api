package de.retest.guistatemachine.api.impl

import java.io.File
import java.util.Arrays

import de.retest.guistatemachine.api.AbstractApiSpec
import de.retest.recheck.ui.descriptors.SutState
import de.retest.surili.commons.actions.{Action, NavigateToAction}
import org.scalatest.BeforeAndAfterEach

class GuiStateMachineImplSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val sut = new GuiStateMachineImpl
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val rootElementC = getRootElement("c", 0)
  private val action0 = new NavigateToAction("http://google.com")
  private val action1 = new NavigateToAction("http://wikipedia.org")

  override def beforeEach() {
    sut.clear()
  }

  "GuiStateMachine" should {
    "not create a new state when using the same root elements" in {
      val s0 = new SutState(Arrays.asList(getRootElement("a", 1)))
      val s0Equal = new SutState(Arrays.asList(getRootElement("a", 1)))
      val differentState = new SutState(Arrays.asList(getRootElement("a", 2)))
      s0.equals(s0Equal) shouldBe true
      s0.hashCode() shouldEqual s0Equal.hashCode()
      differentState.equals(s0) shouldBe false
      differentState.hashCode() should not equal s0.hashCode()
      sut.getAllStates.size shouldEqual 0
      sut.getState(s0)
      sut.getAllStates.size shouldEqual 1
      sut.getState(s0Equal)
      sut.getAllStates.size shouldEqual 1
      sut.getState(differentState)
      sut.getAllStates.size shouldEqual 2
    }

    "add two transitions to two new states for the same action and one transition to another state for another action" in {
      val initialSutState = getSutState
      val initial = sut.getState(initialSutState)
      sut.getAllExploredActions.size shouldEqual 0
      sut.getActionExecutionTimes.size shouldEqual 0

      // execute action0 for the first time
      val s0SutState = new SutState(Arrays.asList(rootElementA))
      val s0 = sut.getState(s0SutState)
      sut.executeAction(initial, action0, s0)
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(action0).to.size shouldEqual 1
      initial.getTransitions(action0).executionCounter shouldEqual 1
      s0.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getActionExecutionTimes.get(action0).isDefined shouldEqual true
      sut.getActionExecutionTimes(action0) shouldEqual 1

      // execute action0 for the second time
      val s1SutState = new SutState(Arrays.asList(rootElementB))
      val s1 = sut.getState(s1SutState)
      sut.executeAction(initial, action0, s1)
      initial.getTransitions.size shouldEqual 1
      initial.getTransitions(action0).to.size shouldEqual 2
      initial.getTransitions(action0).executionCounter shouldEqual 2
      s1.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 1
      sut.getActionExecutionTimes.get(action0).isDefined shouldEqual true
      sut.getActionExecutionTimes(action0) shouldEqual 2

      // execute action1 for the first time
      val s2SutState = new SutState(Arrays.asList(rootElementC))
      val s2 = sut.getState(s2SutState)
      sut.executeAction(initial, action1, s2)
      initial.getTransitions.size shouldEqual 2
      initial.getTransitions(action1).to.size shouldEqual 1
      initial.getTransitions(action1).executionCounter shouldEqual 1
      s2.getTransitions.size shouldEqual 0
      sut.getAllExploredActions.size shouldEqual 2
      sut.getActionExecutionTimes.get(action1).isDefined shouldEqual true
      sut.getActionExecutionTimes(action1) shouldEqual 1
    }

    "store a state for the second access" in {
      val initialSutState = getSutState
      val initialFromAccess0 = sut.getState(initialSutState)
      val initialFromAccess1 = sut.getState(initialSutState)
      initialFromAccess0 shouldEqual initialFromAccess1
    }

    "clear the state machine" in {
      sut.clear()
      sut.getAllExploredActions.isEmpty shouldEqual true
      sut.getActionExecutionTimes.isEmpty shouldEqual true
      sut.getAllStates.isEmpty shouldEqual true
    }

    "save and load" in {
      val filePath = "./target/test_state_machine"
      val oldFile = new File(filePath)

      if (oldFile.exists()) oldFile.delete() shouldEqual true

      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
      val finalSutState = new SutState(Arrays.asList(rootElementC))

      // Create the whole state machine:
      val initialState = sut.getState(initialSutState)
      val finalState = sut.getState(finalSutState)
      sut.executeAction(initialState, action0, finalState)

      // Save the state machine:
      sut.save(filePath)
      val f = new File(filePath)
      f.exists() shouldEqual true
      f.isDirectory shouldEqual false

      // Load the state machine:
      sut.clear()
      sut.load(filePath)

      // Verify the loaded state machine:
      sut.getAllExploredActions.size shouldEqual 1
      sut.getActionExecutionTimes(action0) shouldEqual 1
      sut.getActionExecutionTimes.contains(action1) shouldEqual false
      sut.getAllStates.size shouldEqual 2
      val loadedInitialState = sut.getAllStates(initialSutState)
      val loadedFinalState = sut.getAllStates(finalSutState)
      loadedInitialState.getSutState shouldEqual initialSutState
      loadedInitialState.getTransitions.size shouldEqual 1
      loadedInitialState.getTransitions.contains(action0) shouldEqual true
      val loadedTransition = loadedInitialState.getTransitions(action0)
      loadedTransition.executionCounter shouldEqual 1
      loadedTransition.to.size shouldEqual 1
      loadedTransition.to.head shouldEqual loadedFinalState
      loadedFinalState.getSutState shouldEqual finalSutState
      loadedFinalState.getTransitions.isEmpty shouldEqual true
    }

    "save GML " in {
      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val action0 = new NavigateToAction("http://google.com")
      val action1 = new NavigateToAction("http://wikipedia.org")

      val initialSutState = new SutState(Arrays.asList(rootElementA, rootElementB, rootElementC))
      val finalSutState = new SutState(Arrays.asList(rootElementC))

      // Create the whole state machine:
      sut.clear()
      val initialState = sut.getState(initialSutState)
      val finalState = sut.getState(finalSutState)
      sut.executeAction(initialState, action0, finalState)
      sut.executeAction(initialState, action1, finalState)
      sut.executeAction(finalState, action0, initialState)
      sut.executeAction(finalState, action1, initialState)

      initialState.getTransitions.size shouldEqual 2
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
