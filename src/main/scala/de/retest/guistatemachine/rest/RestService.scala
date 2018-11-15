package de.retest.guistatemachine.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.retest.guistatemachine.api.GuiStateMachineApi

trait RestService {

  def getRoute(guiStateMachineApi: GuiStateMachineApi): Route = {
    val guiStateMachineService = new GuiStateMachineService(guiStateMachineApi)

    get {
      pathSingleSlash {
        complete("GUI State Machine API")
      }
    } ~ guiStateMachineService.getRoute() ~ SwaggerDocService.routes
  }
}
