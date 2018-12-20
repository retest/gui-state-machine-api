package de.retest.guistatemachine.api

import java.util.Arrays

import de.retest.ui.descriptors.SutState

class DescriptorsSpec extends AbstractApiSpec {
  val identifyingAttributesA = getIdentifyingAttributes("a")
  val identifyingAttributesB = getIdentifyingAttributes("b")

  val rootElementA = getRootElement("a")
  val rootElementB = getRootElement("b")
  val descriptorsA = Descriptors(new SutState(Arrays.asList(rootElementA)))
  val descriptorsB = Descriptors(new SutState(Arrays.asList(rootElementB)))

  "Descriptor" should {
    "make sure that identifying attributes with different IDs are not equal" in {
      identifyingAttributesA shouldNot equal(identifyingAttributesB)
      identifyingAttributesA.hashCode() shouldNot equal(identifyingAttributesB.hashCode())
      rootElementA shouldNot equal(rootElementB)
      rootElementA.hashCode() shouldNot equal(rootElementB.hashCode())
    }

    "not equal" in {
      descriptorsA.sutState.getRootElements().size shouldEqual 1
      descriptorsB.sutState.getRootElements().size shouldEqual 1
      descriptorsA shouldNot equal(descriptorsB)
    }

    "equal" in {
      descriptorsA shouldEqual descriptorsA
      descriptorsB shouldEqual descriptorsB
    }
  }
}
