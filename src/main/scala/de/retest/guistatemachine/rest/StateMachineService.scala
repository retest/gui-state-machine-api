package de.retest.guistatemachine.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import de.retest.guistatemachine.persistence.Persistence
import de.retest.guistatemachine.rest.model.{Id, StateMachine}
import io.swagger.annotations.{Api, ApiOperation, ApiResponse, ApiResponses}
import javax.ws.rs.Path
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._

@Api(value = "/state-machine", description = "Gets a state machine")
@Path("/state-machine")
class StateMachineService(persistence: Persistence) extends Directives with DefaultJsonFormats {

  def getRoute(): Route = getStateMachine() ~ deleteStateMachine() ~ postStateMachine()

  @ApiOperation(httpMethod = "GET", response = classOf[StateMachine], value = "Returns a state machine based on the ID")
  @ApiResponses(Array(new ApiResponse(code = 404, message = "State machine not found")))
  def getStateMachine(): Route = get {
    path("state-machine" / LongNumber) { id =>
      val app = persistence.getStateMachine(Id(id))
      app match {
        case Some(x) => complete(x)
        case None    => complete(StatusCodes.NotFound)
      }
    }
  }

  @ApiOperation(httpMethod = "DELETE", response = classOf[akka.http.scaladsl.model.StatusCode], value = "Returns the status code")
  @ApiResponses(
    Array(
      new ApiResponse(code = 200, message = "Successful deletion"),
      new ApiResponse(code = 404, message = "State machine not found")
    ))
  def deleteStateMachine(): Route = delete {
    path("state-machine" / LongNumber) { stateMachineId =>
      val r = persistence.deleteStateMachine(Id(stateMachineId))
      complete(if (r) StatusCodes.OK else StatusCodes.NotFound)
    }
  }

  @ApiOperation(httpMethod = "POST", response = classOf[Id], value = "Returns the ID")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Successful creation")))
  def postStateMachine(): Route = post {
    path("state-machine") {
      val id = persistence.createStateMachine()
      complete(id)
    }
  }
}
