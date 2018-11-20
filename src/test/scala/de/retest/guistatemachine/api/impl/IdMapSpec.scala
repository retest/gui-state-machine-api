package de.retest.guistatemachine.api.impl

import org.scalatest.{Matchers, WordSpec}

class IdMapSpec extends WordSpec with Matchers {

  "IdMapSpec" should {
    "generate new IDs" in {
      val map = IdMap[Int]
      val id0 = map.generateId
      map.values = map.values + (id0 -> 1)
      val id1 = map.generateId
      id0.id shouldEqual 0
      id1.id shouldEqual 1
    }
  }
}
