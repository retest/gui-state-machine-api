package de.retest.guistatemachine.api

/**
  * Interaction from the user with the GUI.
  * TODO #6 Use an abstract representation of actions from retest-model instead of Selenium. The legacy code used `ActionIdentifyingAttributes`.
  */
case class Action(a: org.openqa.selenium.interactions.Action) {

  // TODO #6 Convert abstract representation of actions into string.
  override def toString: String = "Selenium Action"
}
