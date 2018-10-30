package de.retest.guistatemachine.model

import org.scalatest.WordSpec
import org.scalatest.Matchers
import scala.collection.immutable.HashMap

class MapSpec extends WordSpec with Matchers {

  "Map" should {
    "generate new IDs" in {
      val hashMap = new HashMap[Id, Int]
      val map = Map(hashMap)
      val id0 = map.generateId
      map.values = hashMap + (id0 -> 1)
      val id1 = map.generateId
      id0.id shouldEqual 0
      id1.id shouldEqual 1
    }
  }
}