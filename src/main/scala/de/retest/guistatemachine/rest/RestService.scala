package de.retest.guistatemachine.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.retest.guistatemachine.persistence.Persistence
import de.retest.guistatemachine.rest.model.{Action, Actions, Id, State, StateMachine, StateMachines, States, Transition, Transitions}
import spray.json.DefaultJsonProtocol._

trait RestService {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  // formats for unmarshalling and marshalling
  implicit val idFormat = jsonFormat1(Id)

  implicit val actionFormat = jsonFormat0(Action)
  implicit val idMapFormatActions = new JsonFormatForIdMap[Action]
  implicit val actionsFormat = jsonFormat1(Actions)
  implicit val transitionFormat = jsonFormat2(Transition)
  implicit val idMapFormatTransitions = new JsonFormatForIdMap[Transition]
  implicit val transitionsFormat = jsonFormat1(Transitions)
  implicit val stateFormat = jsonFormat1(State)
  implicit val idMapFormatState = new JsonFormatForIdMap[State]
  implicit val statesFormat = jsonFormat1(States)
  implicit val stateMachineFormat = jsonFormat2(StateMachine)
  implicit val idMapFormatStateMachines = new JsonFormatForIdMap[StateMachine]
  implicit val stateMachinesFormat = jsonFormat1(StateMachines)

  def getRoute(persistence: Persistence): Route =
    get {
      pathSingleSlash {
        complete("GUI State Machine API")
      } ~
        path("state-machines") {
          complete(persistence.getStateMachines())
        } ~
        path("state-machine" / LongNumber) { id =>
          val app = persistence.getStateMachine(Id(id))
          app match {
            case Some(x) => complete(x)
            case None    => complete(StatusCodes.NotFound)
          }
        }
    } ~
      post {
        path("create-state-machine") {
          val id = persistence.createStateMachine()
          complete(id)
        }
      } ~ delete {
      path("state-machine" / LongNumber) { stateMachineId =>
        val r = persistence.deleteStateMachine(Id(stateMachineId))
        complete(if (r) StatusCodes.OK else StatusCodes.NotFound)
      }
    }
}