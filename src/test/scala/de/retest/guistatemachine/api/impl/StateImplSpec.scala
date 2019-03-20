package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.AbstractApiSpec

class StateImplSpec extends AbstractApiSpec {
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val sutStateA = createSutState(rootElementA)
  private val sutStateB = createSutState(rootElementB)

  "StateImpl" should {
    "not equal" in {
      val s0 = StateImpl(sutStateA)
      val s1 = StateImpl(sutStateB)
      s0.equals(s1) shouldEqual false
      s0.equals(10) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = StateImpl(sutStateA)
      val s1 = StateImpl(sutStateA)
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = StateImpl(sutStateA)
      s0.toString shouldEqual "sutState=State[descriptor=[]],transitions=Map()"
    }
  }
}
