package de.retest.guistatemachine.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.retest.guistatemachine.api.impl.GuiStateMachineApiImpl
import de.retest.guistatemachine.api.{GuiStateMachine, Id}
import de.retest.guistatemachine.rest.json.DefaultJsonFormats
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class RestServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService with DefaultJsonFormats with BeforeAndAfterAll {

  val sut = getRoute(GuiStateMachineApiImpl)

  override def beforeAll = GuiStateMachineApiImpl.clear()

  override def afterAll = GuiStateMachineApiImpl.clear()

  "The service" should {
    "show the default text for the GET request with the path /" in {
      Get("/") ~> sut ~> check {
        handled shouldEqual true
        val r = responseAs[String]
        r shouldEqual "GUI State Machine API"
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
        handled shouldEqual true
        responseAs[Id] shouldEqual Id(0)
        GuiStateMachineApiImpl.getStateMachine(Id(0)).isDefined shouldEqual true
      }
    }

    "return an empty application for the GET request with the path /state-machine/0" in {
      Get("/state-machine/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        val r = responseAs[GuiStateMachine]
        r.getAllNeverExploredActions.size shouldEqual 0
        r.getAllExploredActions.size shouldEqual 0
        r.getActionExecutionTimes.size shouldEqual 0
      }
    }

    "return status OK for the DELETE request with the path /state-machine/0" in {
      Delete("/state-machine/0") ~> sut ~> check {
        handled shouldEqual true
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
        GuiStateMachineApiImpl.getStateMachine(Id(0)).isEmpty shouldEqual true
      }
    }

    "not handle the GET request with the path /state-machine/bla/hello/bla" in {
      Get("/state-machine/bla/hello/bla") ~> sut ~> check {
        handled shouldEqual false
      }
    }

    // TODO #1 Test getting states and executing actions
  }
}
