package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, SutStateIdentifier}

class StateImplSpec extends AbstractApiSpec {
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val sutStateA = createSutState(rootElementA)
  private val sutStateAIdentifier = new SutStateIdentifier(sutStateA)
  private val sutStateB = createSutState(rootElementB)
  private val sutStateBIdentifier = new SutStateIdentifier(sutStateB)

  "StateImpl" should {
    "not equal" in {
      val s0 = StateImpl(sutStateAIdentifier)
      val s1 = StateImpl(sutStateBIdentifier)
      s0.equals(s1) shouldEqual false
      s0.equals(10) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = StateImpl(sutStateAIdentifier)
      val s1 = StateImpl(sutStateAIdentifier)
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = StateImpl(sutStateAIdentifier)
      s0.toString shouldEqual "sutStateIdentifier=hash=0e4fd44f14d365fae3a7f3579b7ef013e1167e0f4ef6de418367b81edc63450d"
    }
  }
}
