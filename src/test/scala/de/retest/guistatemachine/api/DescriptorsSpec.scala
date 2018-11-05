package de.retest.guistatemachine.api
import scala.collection.immutable.HashSet

class DescriptorsSpec extends AbstractApiSpec {

  "Descriptor" should {
    "not equal" in {
      val d0 = Descriptors(HashSet(getRootElement()))
      val d1 = Descriptors(HashSet(getRootElement()))
      d0.equals(d1) shouldEqual false
    }

    "equal" in {
      val d0 = Descriptors(HashSet(getRootElement()))
      val d1 = Descriptors(HashSet(getRootElement()))
      d0.equals(d1) shouldEqual true
    }
  }
}
