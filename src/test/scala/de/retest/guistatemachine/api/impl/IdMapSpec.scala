package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id
import org.scalatest.{Matchers, WordSpec}

class IdMapSpec extends WordSpec with Matchers {

  "IdMapSpec" should {
    "create a new ID Map" in {
      val map = IdMap[Int](1, 2, 3)
      map.hasElement(Id(0)) shouldEqual true
      map.getElement(Id(0)).get shouldEqual 1
      map.removeElement(Id(0)) shouldEqual true
      map.hasElement(Id(0)) shouldEqual false
    }

    "generate new IDs" in {
      val map = IdMap[Int]()
      val id0 = map.generateId
      map.values = map.values + (id0 -> 1)
      val id1 = map.generateId
      id0.id shouldEqual 0
      id1.id shouldEqual 1
    }
  }
}
