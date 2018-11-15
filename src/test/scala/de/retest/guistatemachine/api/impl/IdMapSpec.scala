package de.retest.guistatemachine.api.impl

import de.retest.guistatemachine.api.Id
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.HashMap

class IdMapSpec extends WordSpec with Matchers {

  "IdMapSpec" should {
    "generate new IDs" in {
      val hashMap = new HashMap[Id, Int]
      val map = IdMap(hashMap)
      val id0 = map.generateId
      map.values = hashMap + (id0 -> 1)
      val id1 = map.generateId
      id0.id shouldEqual 0
      id1.id shouldEqual 1
    }
  }
}
