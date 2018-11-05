package de.retest.guistatemachine.api.impl
import de.retest.guistatemachine.api.{AbstractApiSpec, Action, Descriptors}

import scala.collection.immutable.HashSet

class StateImplSpec extends AbstractApiSpec {

  val descriptorsA = Descriptors(HashSet(getRootElement("a")))
  val descriptorsB = Descriptors(HashSet(getRootElement("b")))
  val action0Mock = getAction()
  val action1Mock = getAction()

  "StateImpl" should {
    "not equal" in {
      val s0 = new StateImpl(descriptorsA, HashSet(Action(action0Mock)))
      val s1 = new StateImpl(descriptorsB, HashSet(Action(action1Mock)))
      s0.equals(s1) shouldEqual false
    }

    "equal" in {
      val s0 = new StateImpl(descriptorsA, HashSet(Action(action0Mock)))
      val s1 = new StateImpl(descriptorsA, HashSet(Action(action1Mock)))
      s0.equals(s1) shouldEqual true
    }
  }
}
