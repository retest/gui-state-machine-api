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

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

class RestServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService {

  lazy val sut = route

  "The service" should {
    "return an empty list for the GET request with the path /applications" in {
      Get("/applications") ~> sut ~> check {
        val r = responseAs[GuiApplications]
        r.applications.size shouldEqual 0
      }
    }

    "allow POST for path /create-application" in {
      Post("/create-application") ~> sut ~> check {
        responseAs[Id] shouldEqual Id(0)
      }
    }

    "return an empty application for the GET request with the path /application/0" in {
      Get("/applications/0") ~> sut ~> check {
        val r = responseAs[GuiApplication]
        r.testSuites.testSuites.size shouldEqual 0
      }
    }

    "return an empty list for the GET request with the path /application/0/test-suites" in {
      Get("/applications/0/test-suites") ~> sut ~> check {
        val r = responseAs[TestSuites]
        r.testSuites.size shouldEqual 0
      }
    }

    "allow POST for path /application/0/create-test-suite" in {
      Post("/application/0/create-test-suite") ~> sut ~> check {
        responseAs[Id] shouldEqual Id(0)
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
