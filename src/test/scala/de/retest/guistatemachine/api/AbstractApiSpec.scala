package de.retest.guistatemachine.api

import java.util.Collections

import de.retest.ui.descriptors._
import de.retest.ui.image.Screenshot
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

abstract class AbstractApiSpec extends WordSpec with Matchers with MockFactory {

  /**
    * The standard constructor of RootElement leads to an exception.
    * Hence, we have to use the constructor with arguments.
    */
  class MockableRootElement
      extends RootElement(
        "retestId",
        new IdentifyingAttributes(Collections.emptyList()),
        new Attributes(),
        new Screenshot("prefix", Array(1, 2, 3), Screenshot.ImageType.PNG),
        Collections.emptyList(),
        "screen0",
        0,
        "My Window"
      )

  /**
    * The identifying attributes and the contained components specify the equality but we mock everything for our tests.
    * @return A new root element which is equal to itself but not to any other root element.
    */
  def getRootElement(): RootElement = {
    val r = mock[MockableRootElement]
    (r.equals _).expects().returning(false)
    (r.equals _).expects(r).returning(true)
    r
  }

  def getAction(): org.openqa.selenium.interactions.Action = mock[org.openqa.selenium.interactions.Action]
}
