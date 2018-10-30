package de.retest.guistatemachine

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._

import de.retest.guistatemachine.persistence.Persistence
import de.retest.guistatemachine.model.GuiApplications
import de.retest.guistatemachine.model.GuiApplication
import de.retest.guistatemachine.model.TestSuite
import de.retest.guistatemachine.model.TestSuites
import de.retest.guistatemachine.model.Id
import de.retest.guistatemachine.persistence.Persistence
import de.retest.guistatemachine.model.StateMachine
import de.retest.guistatemachine.model.Transition
import de.retest.guistatemachine.model.Transitions
import de.retest.guistatemachine.model.Action
import de.retest.guistatemachine.model.Actions
import de.retest.guistatemachine.model.StateMachines
import de.retest.guistatemachine.model.State
import de.retest.guistatemachine.model.States

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

  implicit val testSuiteFormat = jsonFormat0(TestSuite)
  implicit val idMapFormatTestSuites = new JsonFormatForIdMap[TestSuite]
  implicit val testSuitesFormat = jsonFormat1(TestSuites)
  implicit val applicationFormat = jsonFormat1(GuiApplication)
  implicit val idMapFormatApplications = new JsonFormatForIdMap[GuiApplication]
  implicit val applicationsFormat = jsonFormat1(GuiApplications)

  /**
   * Creates the complete route for the REST service with all possible paths.
   * Note that the order of path prefixes is important.
   * For example, if "application/LongNumber" comes before "application/LongNumber/bla", the second path
   * will always be ignored.
   */
  def getRoute(persistence: Persistence): Route =
    get {
      pathSingleSlash {
        complete("GUI State Machine API")
      } ~
        path("state-machines") {
          complete(persistence.getStateMachines())
        } ~
        path("applications") {
          complete(persistence.getApplications())
        } ~
        path("application" / LongNumber / "test-suites") { id =>
          val testSuites = persistence.getTestSuites(Id(id))
          testSuites match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        path("application" / LongNumber / "test-suite" / LongNumber) { (appId, suiteId) =>
          val suite = persistence.getTestSuite(Id(appId), Id(suiteId))
          suite match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        path("application" / LongNumber) { id =>
          val app = persistence.getApplication(Id(id))
          app match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        }
    } ~
      post {
        path("create-application") {
          val id = persistence.addApplication()
          complete(id)
        } ~
          pathPrefix("application" / LongNumber / "create-test-suite") { appId =>
            {
              val id = persistence.addTestSuite(Id(appId))
              complete(id)
            }
          }
      } ~ delete {
        path("application" / LongNumber / "test-suite" / LongNumber) { (appId, suiteId) =>
          val r = persistence.deleteTestSuite(Id(appId), Id(suiteId))
          complete(if (r) StatusCodes.OK else StatusCodes.NotFound)
        }
      } ~ delete {
        path("application" / LongNumber) { id =>
          val r = persistence.deleteApplication(Id(id))
          complete(if (r) StatusCodes.OK else StatusCodes.NotFound)
        }
      }
}