package de.retest.guistatemachine.api

/**
  * Interaction from the user with the GUI.
  * TODO #6 Use an abstract representation of actions from retest-model. The legacy code used `ActionIdentifyingAttributes`.
  * Selenium action types like `org.openqa.selenium.interactions.Action` should not be used since we require an `equals`
  * and `hashCode` method here to use the action as a key for transitions.
  * See [[https://bitbucket.org/retest/surili/src/surili-driver/surili-model/src/main/java/de/retest/surili/model/Action.java]].
  */
@SerialVersionUID(1L)
case class Action(id: Id) extends Serializable
