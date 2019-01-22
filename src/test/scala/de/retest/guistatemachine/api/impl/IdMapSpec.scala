package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id
import org.scalatest.{Matchers, WordSpec}

class IdMapSpec extends WordSpec with Matchers {

  "IdMapSpec" should {
    "create a new ID Map" in {
      val map = IdMap[Int](1, 2, 3)
      map.size shouldEqual 3
      map.hasElement(Id(0)) shouldEqual true
      map.getElement(Id(0)).get shouldEqual 1
      map.removeElement(Id(0)) shouldEqual true
      map.hasElement(Id(0)) shouldEqual false
      map.size shouldEqual 2
      map.addNewElement(4) shouldEqual Id(0)
      map.size shouldEqual 3
      map.clear()
      map.size shouldEqual 0
    }
  }
}
