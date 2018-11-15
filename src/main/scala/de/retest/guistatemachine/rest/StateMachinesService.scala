package de.retest.guistatemachine.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{Directives, Route}
import de.retest.guistatemachine.persistence.Persistence
import de.retest.guistatemachine.rest.model.StateMachines
import io.swagger.annotations.{Api, ApiOperation, ApiResponses}
import javax.ws.rs.Path
import akka.http.scaladsl.server.Directives._

@Api(value = "/state-machines", description = "Operations about state machines")
@Path("/state-machines")
class StateMachinesService(persistence: Persistence) extends Directives with DefaultJsonFormats {

  def getRoute(): Route = getStateMachines()

  @ApiOperation(httpMethod = "GET", response = classOf[StateMachines], value = "Returns all state machines")
  @ApiResponses(Array())
  def getStateMachines(): Route =
    path("state-machines") {
      complete(persistence.getStateMachines())
    }
}
