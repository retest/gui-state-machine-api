package de.retest.guistatemachine.api

import java.util.Arrays

import de.retest.recheck.ui.descriptors._
import de.retest.recheck.ui.image.Screenshot
import de.retest.surili.commons.actions.{ActionType, NavigateToAction}
import org.scalatest.{Matchers, WordSpec}

abstract trait AbstractApiSpec extends WordSpec with Matchers {
  protected val rootElementA = getRootElement("a", 0)
  protected val rootElementB = getRootElement("b", 0)
  protected val rootElementC = getRootElement("c", 0)
  protected val action0 = new NavigateToAction("http://google.com")
  protected val action0Identifier = new ActionIdentifier(action0)
  protected val actionType0 = ActionType.fromAction(action0)
  protected val action1 = new NavigateToAction("http://wikipedia.org")
  protected val action1Identifier = new ActionIdentifier(action1)
  protected val actionType1 = ActionType.fromAction(action1)
  protected val unexploredActionTypes = Set(actionType0, actionType1)

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
