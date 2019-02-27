package de.retest.guistatemachine.api.impl

import java.util.Arrays

import de.retest.guistatemachine.api.AbstractApiSpec
import de.retest.recheck.ui.descriptors.SutState

class StateImplSpec extends AbstractApiSpec {
  private val rootElementA = getRootElement("a", 0)
  private val rootElementB = getRootElement("b", 0)
  private val sutStateA = new SutState(Arrays.asList(rootElementA))
  private val sutStateB = new SutState(Arrays.asList(rootElementB))

  "StateImpl" should {
    "not equal" in {
      val s0 = new StateImpl(sutStateA)
      val s1 = new StateImpl(sutStateB)
      s0.equals(s1) shouldEqual false
      s0.equals(10) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = new StateImpl(sutStateA)
      val s1 = new StateImpl(sutStateA)
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = new StateImpl(sutStateA)
      s0.toString shouldEqual "sutState=State[descriptor=[]],transitions=Map()"
    }
  }
}
