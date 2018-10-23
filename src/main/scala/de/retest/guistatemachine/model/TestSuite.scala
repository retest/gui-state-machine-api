package de.retest.guistatemachine.model

class TestSuite {
  private val cases = Set[TestCase]()

  def size = cases.size

  def length = cases.foldLeft(0)((size, c) => size + c.length)
}