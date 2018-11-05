package de.retest.guistatemachine.rest

import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpCharset
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.retest.guistatemachine.model.GuiApplications
import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.model.GuiApplication
import de.retest.guistatemachine.model.TestSuites
import de.retest.guistatemachine.persistence.Persistence
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.MediaTypes
import de.retest.guistatemachine.model.TestSuite
import akka.http.scaladsl.model.StatusCode

class RestServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService {

  val persistence = new Persistence
  val sut = getRoute(persistence)

  "The service" should {
    "show the default text for the GET request with the path /" in {
      Get("/") ~> sut ~> check {
        handled shouldEqual true
        val r = responseAs[String]
        r shouldEqual "GUI State Machine API"
      }
    }

    "return an empty list for the GET request with the path /applications" in {
      Get("/applications") ~> sut ~> check {
        handled shouldEqual true
        mediaType shouldEqual MediaTypes.`application/json`
        val r = responseAs[GuiApplications]
        r.apps.values.size shouldEqual 0
      }
    }

    "fail for the GET request with the path /application/0" in {
      Get("/application/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "fail for the DELETE request with the path /application/0" in {
      Delete("/application/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "fail for the GET request with the path /application/0/test-suites" in {
      Get("/application/0/test-suites") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "allow POST for path /create-application" in {
      Post("/create-application") ~> sut ~> check {
        handled shouldEqual true
        responseAs[Id] shouldEqual Id(0)
        persistence.getApplications.apps.values.size shouldEqual 1
      }
    }

    "return an empty application for the GET request with the path /application/0" in {
      Get("/application/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        val r = responseAs[GuiApplication]
        r.testSuites.suites.values.size shouldEqual 0
      }
    }

    "return an empty list for the GET request with the path /application/0/test-suites" in {
      Get("/application/0/test-suites") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        val r = responseAs[TestSuites]
        r.suites.values.size shouldEqual 0
      }
    }

    "fail for the GET request with the path /application/0/test-suite/0" in {
      Get("/application/0/test-suite/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "fail for the DELETE request with the path /application/0/test-suite/0" in {
      Delete("/application/0/test-suite/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "allow POST for path /application/0/create-test-suite" in {
      Post("/application/0/create-test-suite") ~> sut ~> check {
        handled shouldEqual true
        responseAs[Id] shouldEqual Id(0)
        persistence.getTestSuites(Id(0)).get.suites.values.size shouldEqual 1
      }
    }

    "return an empty test suite for the GET request with the path /application/0/test-suite/0" in {
      Get("/application/0/test-suite/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        val r = responseAs[TestSuite]
        // TODO There is no content in a test suite at the moment
      }
    }

    "return status OK for the DELETE request with the path /application/0/test-suite/0" in {
      Delete("/application/0/test-suite/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
        persistence.getTestSuites(Id(0)).get.suites.values.size shouldEqual 0
      }
    }

    "return status OK for the DELETE request with the path /application/0" in {
      Delete("/application/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
        persistence.getApplications().apps.values.size shouldEqual 0
      }
    }

    "not handle the GET request with the path /applications/bla/hello/bla" in {
      Get("/applications/bla/hello/bla") ~> sut ~> check {
        handled shouldEqual false
        //mediaType shouldEqual MediaTypes.`application/json`
        //val r = responseAs[GuiApplications]
        //r.apps.values.size shouldEqual 0
      }
    }
  }
}
