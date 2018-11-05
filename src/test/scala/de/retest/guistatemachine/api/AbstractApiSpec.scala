package de.retest.guistatemachine.api

import java.util.Collections

import de.retest.ui.Path
import de.retest.ui.descriptors._
import de.retest.ui.image.Screenshot
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import java.util.Arrays


abstract class AbstractApiSpec extends WordSpec with Matchers with MockFactory {

  def getIdentifyingAttributes(id: String): IdentifyingAttributes =
    new IdentifyingAttributes(Arrays.asList(new StringAttribute("a", id), new StringAttribute("b", id), new StringAttribute("c", id)))

  /**
    * The identifying attributes and the contained components specify the equality.
    * @param id If the ID is equal the returned root element will be equal.
    * @return A new root element which is equal to itself but not to any other root element.
    */
  def getRootElement(id: String): RootElement = new RootElement(
    "retestId",
    getIdentifyingAttributes(id),
    new Attributes(),
    new Screenshot("prefix", Array(1, 2, 3), Screenshot.ImageType.PNG),
    Collections.emptyList(),
    "screen0",
    0,
    "My Window"
  )

  def getAction(): org.openqa.selenium.interactions.Action = mock[org.openqa.selenium.interactions.Action]
}
