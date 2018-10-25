package de.retest.guistatemachine.furrermodel

class TestSuite {
  private val cases = Set[TestCase]()

  def size = cases.size

  def length = cases.foldLeft(0)((size, c) => size + c.length)
}