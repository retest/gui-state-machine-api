package de.retest.guistatemachine.model

class TestCase(app: GuiApplication) {
  private val actions = Seq[UIAction]()

  def length = actions.size

  def isValid = true // it is valid if all GUI actions can be executed

  def getUiPath = new UIPath(new PathState(app.getInitialState)) // TODO generate the correct path, with the common initial state
}