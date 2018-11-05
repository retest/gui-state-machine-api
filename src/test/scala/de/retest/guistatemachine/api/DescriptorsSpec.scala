package de.retest.guistatemachine.api

class DescriptorsSpec extends AbstractApiSpec {

  val identifyingAttributesA = getIdentifyingAttributes("a")
  val identifyingAttributesB = getIdentifyingAttributes("b")

  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val descriptorsA = Descriptors(Set(rootElementA))
  val descriptorsB = Descriptors(Set(rootElementB))

  "Descriptor" should {
    "make sure that identifying attributes with different IDs are not equal" in {
      identifyingAttributesA shouldNot equal(identifyingAttributesB)
      identifyingAttributesA.hashCode() shouldNot equal(identifyingAttributesB.hashCode())
      rootElementA shouldNot equal(rootElementB)
      rootElementA.hashCode() shouldNot equal(rootElementB.hashCode())
    }

    "not equal" in {
      descriptorsA.rootElements.size shouldEqual 1
      descriptorsB.rootElements.size shouldEqual 1
      descriptorsA shouldNot equal(descriptorsB)
    }

    "equal" in {
      descriptorsA shouldEqual descriptorsA
      descriptorsB shouldEqual descriptorsB
    }
  }
}
