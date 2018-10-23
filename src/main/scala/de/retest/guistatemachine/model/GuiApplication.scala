package de.retest.guistatemachine.model

/**
 * The tested GUI application with an initial state and a number of test suites.
 */
class GuiApplication(initialState: State, testSuites: Seq[TestSuite]) {

  def getTestSuites: Seq[TestSuite] = testSuites

  def getInitialState: State = initialState
}