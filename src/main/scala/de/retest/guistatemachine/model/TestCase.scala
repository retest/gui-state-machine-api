package de.retest.guistatemachine.model

class TestCase(initialState: State) {
  private val actions = Seq[UIAction]()

  def length = actions.size

  def isValid = true // it is valid if all GUI actions can be executed

  def getUiPath = new UIPath(new PathState(initialState)) // TODO #2 generate the correct path, with the common initial state
}