package de.retest.guistatemachine.model

/**
 * The tested GUI application with an initial state and a number of test suites.
 *
 * @param id This ID is for the REST API only.
 */
class GuiApplication(val id : Long, initialState: State, testSuites: Seq[TestSuite]) {

  def getTestSuites: Seq[TestSuite] = testSuites

  def getInitialState: State = initialState
}