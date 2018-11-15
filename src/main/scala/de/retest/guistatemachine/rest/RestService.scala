package de.retest.guistatemachine.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.retest.guistatemachine.persistence.Persistence

trait RestService {
  def getRoute(persistence: Persistence): Route = {
    val stateMachinesService = new StateMachinesService(persistence)
    val stateMachineService = new StateMachineService(persistence)

    get {
      pathSingleSlash {
        complete("GUI State Machine API")
      }
    } ~ stateMachinesService.getRoute() ~ stateMachineService.getRoute() ~ getFromResourceDirectory("swagger") ~ SwaggerDocService.routes
  }
  // TODO #1 Add static Swagger UI files to ~ path("swagger") { getFromResource("swagger/index.html") }
}
