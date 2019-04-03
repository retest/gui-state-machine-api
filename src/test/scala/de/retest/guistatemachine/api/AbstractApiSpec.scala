package de.retest.guistatemachine.api

import java.util.Arrays

import de.retest.recheck.ui.descriptors._
import de.retest.recheck.ui.image.Screenshot
import org.scalatest.{Matchers, WordSpec}

abstract trait AbstractApiSpec extends WordSpec with Matchers {

  def createSutState(rootElements: RootElement*): SutState = new SutState(Arrays.asList(rootElements: _*))

  /**
    * Creates a new identifying attributes collection which should only match other identifying attributes with the same ID.
    *
    * @param id The ID is used as value for different attributes.
    * @return The identifying attributes.
    */
  def getIdentifyingAttributes(id: String): IdentifyingAttributes =
    new IdentifyingAttributes(Arrays.asList(new StringAttribute("a", id), new StringAttribute("b", id), new StringAttribute("c", id)))

  /**
    * The identifying attributes and the contained components specify the equality.
    *
    * @param id  This value is a criteria for equality of the returned element.
    * @param numberOfContainedComponents This value is a criteria for equality of the returned element.
    * @return A new root element which is equal to itself but not to any other root element.
    */
  def getRootElement(id: String, numberOfContainedComponents: Int): RootElement = {
    val r = new RootElement(
      "retestId",
      getIdentifyingAttributes(id),
      new Attributes(),
      new Screenshot("prefix", Array(1, 2, 3), Screenshot.ImageType.PNG),
      "screen0",
      0,
      "My Window"
    )
    if (numberOfContainedComponents > 0) {
      r.addChildren(scala.collection.JavaConverters.seqAsJavaList[Element](0 to numberOfContainedComponents map { _ =>
        getRootElement("x", 0)
      }))
    }
    r
  }
}
