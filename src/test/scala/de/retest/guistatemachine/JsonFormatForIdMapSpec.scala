package de.retest.guistatemachine

import org.scalatest.WordSpec
import org.scalatest.Matchers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._
import de.retest.guistatemachine.model.TestSuites
import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.TestSuite
import scala.collection.immutable.HashMap
import de.retest.guistatemachine.model.Map

class JsonFormatForIdMapSpec extends WordSpec with Matchers {

  implicit val idFormat = jsonFormat1(Id)
  implicit val testSuiteFormat = jsonFormat0(TestSuite)
  implicit val hashMapFormatTestSuites = new JsonFormatForIdMap[TestSuite]
  implicit val testSuitesFormat = jsonFormat1(TestSuites)

  "The JSON format" should {
    "convert an empty test suite into JSON and back" in {
      val testSuites = TestSuites(Map(new HashMap[Id, TestSuite]()))
      val json = testSuites.toJson
      json.toString shouldEqual "{\"suites\":{}}"
      val transformedTestSuites = json.convertTo[TestSuites]
      transformedTestSuites.suites.values.isEmpty shouldEqual true
    }

    "convert a test suite with elements into JSON and back" in {
      val testSuites = TestSuites(Map(new HashMap[Id, TestSuite]()))
      testSuites.suites.values = testSuites.suites.values + (Id(0) -> TestSuite())
      val json = testSuites.toJson
      json.toString shouldEqual "{\"suites\":{\"0\":{}}}"
      val transformedTestSuites = json.convertTo[TestSuites]
      transformedTestSuites.suites.values.isEmpty shouldEqual false
      transformedTestSuites.suites.values.contains(Id(0)) shouldEqual true
    }
  }

}