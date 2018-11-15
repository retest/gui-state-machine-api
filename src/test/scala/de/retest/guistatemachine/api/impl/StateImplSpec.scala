package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

class StateImplSpec extends AbstractApiSpec {

  val descriptorsA = Descriptors(Set(getRootElement("a")))
  val descriptorsB = Descriptors(Set(getRootElement("b")))
  val action0Mock = getAction()
  val action1Mock = getAction()

  "StateImpl" should {
    "not equal" in {
      val s0 = new StateImpl(descriptorsA, Set(Action(action0Mock)))
      val s1 = new StateImpl(descriptorsB, Set(Action(action1Mock)))
      s0.equals(s1) shouldEqual false
      s0.equals(null) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = new StateImpl(descriptorsA, Set(Action(action0Mock)))
      val s1 = new StateImpl(descriptorsA, Set(Action(action1Mock)))
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = new StateImpl(descriptorsA, Set(Action(action0Mock)))
      s0.toString shouldEqual "descriptors=Descriptors(Set()),neverExploredActions=Set(Selenium Action),transitions=Map()"
    }

    "have a random action" in {
      val s0 = new StateImpl(descriptorsA, Set(Action(action0Mock)))
      s0.getRandomAction().isDefined shouldEqual true
    }

    "have no random action" in {
      val s0 = new StateImpl(descriptorsA, Set())
      s0.getRandomAction().isEmpty shouldEqual true
    }
  }
}
