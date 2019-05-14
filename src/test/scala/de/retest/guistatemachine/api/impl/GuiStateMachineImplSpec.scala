package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, ActionIdentifier}
import de.retest.surili.commons.actions.NavigateToAction
import org.scalatest.BeforeAndAfterEach

class GuiStateMachineImplSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val sut = new GuiStateMachineImpl
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val rootElementC = getRootElement("c", 0)
  private val action0 = new NavigateToAction("http://google.com")
  private val action0Identifier = new ActionIdentifier(action0)
  private val action1 = new NavigateToAction("http://wikipedia.org")
  private val action1Identifier = new ActionIdentifier(action1)

  override def beforeEach() {
    sut.clear()
  }

  "GuiStateMachine" should {
    "not create a new state when using the same root elements" in {
      val s0 = createSutState(getRootElement("a", 1))
      val s0Equal = createSutState(getRootElement("a", 1))
      val differentState = createSutState(getRootElement("a", 2))
      s0.equals(s0Equal) shouldBe true
      s0.hashCode() shouldEqual s0Equal.hashCode()
      differentState.equals(s0) shouldBe false
      differentState.hashCode() should not equal s0.hashCode()
      sut.getAllStates.size shouldEqual 0
      sut.createState(s0, 0)
      sut.getAllStates.size shouldEqual 1
      the[RuntimeException] thrownBy sut.createState(s0Equal, 0)
      sut.getAllStates.size shouldEqual 1
      sut.createState(differentState, 0)
      sut.getAllStates.size shouldEqual 2
    }

    "add two transitions to two new states for the same action and two transitions for the same action to another state" in {
      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val initial = sut.createState(initialSutState, 2)
      initial.getNeverExploredActionTypesCounter shouldEqual 2

      // execute action0 for the first time
      val s0SutState = createSutState(rootElementA)
      val s0 = sut.createState(s0SutState, 2)
      sut.executeAction(initial, action0, s0) shouldEqual 1
      initial.getOutgoingActionTransitions.size shouldEqual 1
      initial.getOutgoingActionTransitions(action0Identifier).states.size shouldEqual 1
      initial.getOutgoingActionTransitions(action0Identifier).executionCounter shouldEqual 1
      initial.getIncomingActionTransitions.size shouldEqual 0
      initial.getNeverExploredActionTypesCounter shouldEqual 1
      s0.getOutgoingActionTransitions.size shouldEqual 0
      s0.getIncomingActionTransitions.size shouldEqual 1
      s0.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1
      s0.getIncomingActionTransitions(action0Identifier).executionCounter shouldEqual 1
      s0.getNeverExploredActionTypesCounter shouldEqual 2

      // execute action0 for the second time
      val s1SutState = createSutState(rootElementB)
      val s1 = sut.createState(s1SutState, 2)
      sut.executeAction(initial, action0, s1) shouldEqual 2
      initial.getOutgoingActionTransitions.size shouldEqual 1
      initial.getOutgoingActionTransitions(action0Identifier).states.size shouldEqual 2
      initial.getOutgoingActionTransitions(action0Identifier).executionCounter shouldEqual 2
      initial.getIncomingActionTransitions.size shouldEqual 0
      initial.getNeverExploredActionTypesCounter shouldEqual 1
      s1.getOutgoingActionTransitions.size shouldEqual 0
      s1.getIncomingActionTransitions.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).executionCounter shouldEqual 1
      s1.getNeverExploredActionTypesCounter shouldEqual 2

      // execute action1 for the first time
      val s2SutState = createSutState(rootElementC)
      val s2 = sut.createState(s2SutState, 2)
      sut.executeAction(initial, action1, s2) shouldEqual 1
      initial.getOutgoingActionTransitions.size shouldEqual 2
      initial.getOutgoingActionTransitions(action1Identifier).states.size shouldEqual 1
      initial.getOutgoingActionTransitions(action1Identifier).executionCounter shouldEqual 1
      initial.getIncomingActionTransitions.size shouldEqual 0
      initial.getNeverExploredActionTypesCounter shouldEqual 0
      s2.getOutgoingActionTransitions.size shouldEqual 0
      s2.getIncomingActionTransitions.size shouldEqual 1
      s2.getIncomingActionTransitions(action1Identifier).states.size shouldEqual 1
      s2.getIncomingActionTransitions(action1Identifier).executionCounter shouldEqual 1
      s2.getNeverExploredActionTypesCounter shouldEqual 2

      // execute action1 for the second time but from s1SutState to create one incoming action from two different states
      sut.executeAction(s1, action1, s2) shouldEqual 1
      s1.getOutgoingActionTransitions.size shouldEqual 1
      s1.getOutgoingActionTransitions(action1Identifier).states.size shouldEqual 1
      s1.getOutgoingActionTransitions(action1Identifier).executionCounter shouldEqual 1
      s1.getIncomingActionTransitions.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).states.size shouldEqual 1
      s1.getIncomingActionTransitions(action0Identifier).executionCounter shouldEqual 1
      s1.getNeverExploredActionTypesCounter shouldEqual 1
      s2.getOutgoingActionTransitions.size shouldEqual 0
      s2.getIncomingActionTransitions.size shouldEqual 1
      s2.getIncomingActionTransitions(action1Identifier).states shouldEqual Set(initial, s1)
      s2.getIncomingActionTransitions(action1Identifier).executionCounter shouldEqual 2
      s2.getNeverExploredActionTypesCounter shouldEqual 2
    }

    "store a state for the second access" in {
      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)
      val initialFromAccess0 = sut.getState(initialSutState)
      val initialFromAccess1 = sut.getState(initialSutState)
      initialFromAccess0 shouldEqual initialFromAccess1
    }

    "clear the state machine" in {
      sut.clear()
      sut.getAllStates.isEmpty shouldEqual true
    }
  }
}
