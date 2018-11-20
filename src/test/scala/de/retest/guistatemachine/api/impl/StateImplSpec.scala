package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors, Id}

class StateImplSpec extends AbstractApiSpec {

  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val descriptorsA = Descriptors(Set(rootElementA))
  val descriptorsB = Descriptors(Set(rootElementB))
  val action0 = Action(Id(0))
  val action1 = Action(Id(1))

  "StateImpl" should {
    "not equal" in {
      val s0 = new StateImpl(descriptorsA, Set(action0))
      val s1 = new StateImpl(descriptorsB, Set(action1))
      s0.equals(s1) shouldEqual false
      s0.equals(null) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = new StateImpl(descriptorsA, Set(action0))
      val s1 = new StateImpl(descriptorsA, Set(action1))
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = new StateImpl(descriptorsA, Set(action0))
      s0.toString shouldEqual "descriptors=Descriptors(Set()),neverExploredActions=Set(Action(Id(0))),transitions=Map()"
    }

    "have a random action" in {
      val s0 = new StateImpl(descriptorsA, Set(action0))
      s0.getRandomAction().isDefined shouldEqual true
    }

    "have no random action" in {
      val s0 = new StateImpl(descriptorsA, Set())
      s0.getRandomAction().isEmpty shouldEqual true
    }
  }
}
