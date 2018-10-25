package de.retest.guistatemachine.model

final case class TestSuites(testSuites: Map[TestSuite]) {
  // TODO Generate IDs in a better way. Maybe random numbers until one unused element is found?
  def generateId: Id = this.synchronized { if (testSuites.values.isEmpty) Id(0) else Id(testSuites.values.keySet.max.id + 1) }
}