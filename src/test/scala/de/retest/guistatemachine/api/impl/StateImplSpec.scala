package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, SutStateIdentifier}
import de.retest.surili.commons.actions.ActionType
import de.retest.surili.commons.test.TestUtil

class StateImplSpec extends AbstractApiSpec {
  private val sutStateA = TestUtil.createSutState(rootElementA)
  private val sutStateAIdentifier = new SutStateIdentifier(sutStateA)
  private val sutStateB = TestUtil.createSutState(rootElementB)
  private val sutStateBIdentifier = new SutStateIdentifier(sutStateB)

  "StateImpl" should {
    "not equal" in {
      val s0 = StateImpl(sutStateAIdentifier, Set(ActionType.fromAction(action0)))
      val s1 = StateImpl(sutStateBIdentifier, Set(ActionType.fromAction(action0)))
      s0.equals(s1) shouldEqual false
      s0.equals(10) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = StateImpl(sutStateAIdentifier, Set(ActionType.fromAction(action0)))
      val s1 = StateImpl(sutStateAIdentifier, Set(ActionType.fromAction(action0), ActionType.fromAction(action1)))
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = StateImpl(sutStateAIdentifier, unexploredActionTypes)
      s0.toString shouldEqual "[sutState=State[My Window], hash=1d09863c]"
    }
  }
}
