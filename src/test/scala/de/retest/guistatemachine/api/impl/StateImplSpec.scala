package de.retest.guistatemachine.api.impl

import java.util.Arrays

import de.retest.guistatemachine.api.{AbstractApiSpec, Descriptors}
import de.retest.surili.model.{Action, NavigateToAction}
import de.retest.ui.descriptors.SutState

class StateImplSpec extends AbstractApiSpec {
  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val descriptorsA = Descriptors(new SutState(Arrays.asList((rootElementA))))
  val descriptorsB = Descriptors(new SutState(Arrays.asList((rootElementB))))
  val action0 = new NavigateToAction("http://google.com")
  val action1 = new NavigateToAction("http://wikipedia.org")

  "StateImpl" should {
    "not equal" in {
      val s0 = new StateImpl(descriptorsA, Set[Action](action0))
      val s1 = new StateImpl(descriptorsB, Set[Action](action1))
      s0.equals(s1) shouldEqual false
      s0.equals(10) shouldEqual false
      s0.hashCode() should not equal s1.hashCode()
    }

    "equal" in {
      val s0 = new StateImpl(descriptorsA, Set[Action](action0))
      val s1 = new StateImpl(descriptorsA, Set[Action](action1))
      s0.equals(s1) shouldEqual true
      s0.hashCode() shouldEqual s1.hashCode()
    }

    "be converted into a string" in {
      val s0 = new StateImpl(descriptorsA, Set[Action](action0))
      s0.toString shouldEqual "descriptors=Descriptors(State[descriptor=[]]),neverExploredActions=Set(NavigateToAction(url=http://google.com)),transitions=Map()"
    }

    "have a random action" in {
      val s0 = new StateImpl(descriptorsA, Set[Action](action0))
      s0.getRandomAction().isDefined shouldEqual true
    }

    "have no random action" in {
      val s0 = new StateImpl(descriptorsA, Set())
      s0.getRandomAction().isEmpty shouldEqual true
    }
  }
}
