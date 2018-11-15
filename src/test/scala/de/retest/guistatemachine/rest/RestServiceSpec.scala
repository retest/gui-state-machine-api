package de.retest.guistatemachine.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.retest.guistatemachine.persistence.Persistence
import org.scalatest.{Matchers, WordSpec}

class RestServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService with DefaultJsonFormats {

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

    "return an empty list for the GET request with the path /state-machines" in {
      Get("/state-machines") ~> sut ~> check {
        import de.retest.guistatemachine.rest.model.StateMachines
        handled shouldEqual true
        mediaType shouldEqual MediaTypes.`application/json`
        val r = responseAs[StateMachines]
        r.stateMachines.values.size shouldEqual 0
      }
    }

    "fail for the GET request with the path /state-machine/0" in {
      Get("/state-machine/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "fail for the DELETE request with the path /state-machine/0" in {
      Delete("/state-machine/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.NotFound
      }
    }

    "allow POST for path /state-machine" in {
      Post("/state-machine") ~> sut ~> check {
        import de.retest.guistatemachine.rest.model.Id
        handled shouldEqual true
        responseAs[Id] shouldEqual Id(0)
        persistence.getStateMachines().stateMachines.values.size shouldEqual 1
      }
    }

    "return an empty application for the GET request with the path /state-machine/0" in {
      Get("/state-machine/0") ~> sut ~> check {
        import de.retest.guistatemachine.rest.model.StateMachine
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        val r = responseAs[StateMachine]
        r.states.states.values.size shouldEqual 1
        r.actions.actions.values.size shouldEqual 0
      }
    }

    "return status OK for the DELETE request with the path /state-machine/0" in {
      Delete("/state-machine/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
        persistence.getStateMachines().stateMachines.values.size shouldEqual 0
      }
    }

    "not handle the GET request with the path /state-machines/bla/hello/bla" in {
      Get("/state-machines/bla/hello/bla") ~> sut ~> check {
        handled shouldEqual false
        //mediaType shouldEqual MediaTypes.`application/json`
        //val r = responseAs[GuiApplications]
        //r.apps.values.size shouldEqual 0
      }
    }
  }
}
