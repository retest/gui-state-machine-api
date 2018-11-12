package de.retest.guistatemachine.rest.model

import org.scalatest.{Matchers, WordSpec}

class IdSpec extends WordSpec with Matchers {

  "Id" should {
    "compare" in {
      val id0 = Id(0)
      val id1 = Id(1)
      id0.compare(id0) shouldEqual 0
      id0.compare(id1) shouldEqual -1
      id1.compare(id0) shouldEqual 1
    }
  }
}
