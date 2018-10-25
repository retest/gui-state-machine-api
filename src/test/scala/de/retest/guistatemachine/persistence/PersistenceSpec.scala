package de.retest.guistatemachine.persistence

import org.scalatest.Matchers
import org.scalatest.WordSpec
import de.retest.guistatemachine.model.Id

class PersistenceSpec extends WordSpec with Matchers {
  val sut = new Persistence

  "The pesistence" should {
    "allow adding one test suite" in {
      sut.getApplications().applications.values.size shouldEqual 0
      sut.addApplication().id shouldEqual 0
      sut.getApplications().applications.values.size shouldEqual 1
      sut.getTestSuites(Id(0)).get.testSuites.values.size shouldEqual 0
      sut.addTestSuite(Id(0)).get.id shouldEqual 0
      sut.getTestSuites(Id(0)).get.testSuites.values.size shouldEqual 1
      val s = sut.getTestSuite(Id(0), Id(0))
      s.isEmpty shouldEqual false
    }
  }
}