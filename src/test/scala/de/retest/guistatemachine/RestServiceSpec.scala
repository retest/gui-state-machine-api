package de.retest.guistatemachine

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

class RestServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService {

  val sut = getRoute(new Persistence)

  "The service" should {
    "show the default text for the GET request with the path /" in {
      Get("/") ~> sut ~> check {
        val r = responseAs[String]
        r shouldEqual "GUI State Machine API"
      }
    }

    "return an empty list for the GET request with the path /applications" in {
      Get("/applications") ~> sut ~> check {
        handled shouldEqual true
        mediaType shouldEqual MediaTypes.`application/json`
        val r = responseAs[GuiApplications]
        r.values.size shouldEqual 0
      }
    }

    "allow POST for path /create-application" in {
      Post("/create-application") ~> sut ~> check {
        responseAs[Id] shouldEqual Id(0)
      }
    }

    "return an empty application for the GET request with the path /application/0" in {
      Get("/applications/0") ~> sut ~> check {
        // TODO Print response here
        println("Response: " + responseAs[String])
        val r = responseAs[GuiApplication]
        r.testSuites.values.size shouldEqual 0
      }
    }

    "return an empty list for the GET request with the path /application/0/test-suites" in {
      Get("/applications/0/test-suites") ~> sut ~> check {
        val r = responseAs[TestSuites]
        r.values.size shouldEqual 0
      }
    }

    "allow POST for path /application/0/create-test-suite" in {
      Post("/application/0/create-test-suite") ~> sut ~> check {
        responseAs[Id] shouldEqual Id(0)
      }
    }
  }
}
