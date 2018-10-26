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

trait RestService {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  // formats for unmarshalling and marshalling
  implicit val idFormat = jsonFormat1(Id)
  implicit val testSuiteFormat = jsonFormat0(TestSuite)
  implicit val hashMapFormatTestSuites = new JsonFormatForIdMap[TestSuite]
  implicit val testSuitesFormat = jsonFormat1(TestSuites)
  implicit val applicationFormat = jsonFormat1(GuiApplication)
  implicit val hashMapFormatApplications = new JsonFormatForIdMap[GuiApplication]
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
        path("applications") {
          complete(persistence.getApplications())
        } ~
        pathPrefix("application" / LongNumber / "test-suites") { id =>
          val testSuites = persistence.getTestSuites(Id(id))
          testSuites match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        pathPrefix("application" / LongNumber / "test-suite" / LongNumber) { (appId, suiteId) =>
          val suite = persistence.getTestSuite(Id(appId), Id(suiteId))
          suite match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        pathPrefix("application" / LongNumber) { id =>
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
        pathPrefix("application" / LongNumber) { id =>
          val r = persistence.deleteApplication(Id(id))
          complete(StatusCodes.OK)
        }
      }

}