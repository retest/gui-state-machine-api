package de.retest.guistatemachine.api

import de.retest.surili.commons.actions.{ActionType, NavigateToAction}
import de.retest.surili.commons.test.TestUtil
import org.scalatest.{Matchers, WordSpec}

abstract trait AbstractApiSpec extends WordSpec with Matchers {
  protected val rootElementA = TestUtil.getRootElement("a", 0)
  protected val rootElementB = TestUtil.getRootElement("b", 0)
  protected val rootElementC = TestUtil.getRootElement("c", 0)
  protected val action0 = new NavigateToAction("http://google.com")
  protected val action0Identifier = new ActionIdentifier(action0)
  protected val actionType0 = ActionType.fromAction(action0)
  protected val action1 = new NavigateToAction("http://wikipedia.org")
  protected val action1Identifier = new ActionIdentifier(action1)
  protected val actionType1 = ActionType.fromAction(action1)
  protected val unexploredActionTypes = Set(actionType0, actionType1)
}
