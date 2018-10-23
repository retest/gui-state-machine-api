package de.retest.guistatemachine

import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpCharset

class WebServerSpec extends WordSpec with Matchers with ScalatestRouteTest {

  lazy val sut = WebServer.getRoute

  "The service" should {

    "return a greeting for GET requests to the path /hello" in {
      // tests:
      Get("/hello") ~> sut ~> check {
        responseAs[String] shouldEqual "Hello!"
      }
    }

    "not allow POST for path /hello" in {
      Post("/hello") ~> Route.seal(sut) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> sut ~> check {
        handled shouldBe false
      }
    }

    "not find any root path" in {
      Get() ~> Route.seal(sut) ~> check {
        status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual "The requested resource could not be found."
      }
    }
  }
}
