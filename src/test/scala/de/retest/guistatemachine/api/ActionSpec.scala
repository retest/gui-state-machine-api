package de.retest.guistatemachine.api

import org.scalatest.{Matchers, WordSpec}

class ActionSpec extends WordSpec with Matchers {

  "Action" should {
    "equal and not equal" in {
      val action0 = Action(Id(0))
      val action1 = Action(Id(1))
      action0.equals(action0) shouldEqual true
      action0.equals(action1) shouldEqual false
    }

    "toString" in {
      val action0 = Action(Id(0))
      action0.toString shouldEqual "Action(Id(0))"
    }
  }
}
