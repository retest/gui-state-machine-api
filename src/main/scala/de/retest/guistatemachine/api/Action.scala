package de.retest.guistatemachine.api

/**
  * Interaction from the user with the GUI.
  */
case class Action(a: org.openqa.selenium.interactions.Action) {

  // TODO #5 Convert abstract representation of actions into string.
  override def toString: String = "Selenium Action"
}