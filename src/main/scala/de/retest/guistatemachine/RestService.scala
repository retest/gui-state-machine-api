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

import java.util.LinkedList

// domain model
// TODO #1 Id should use Long and the REST paths as well. Use concurrent hash maps with the IDs and generate new IDs for new items.
final case class Id(id: Int)
final case class GuiApplication() {
  val testSuites = TestSuites()
}
final case class GuiApplications() {
  val applications = new LinkedList[GuiApplication]
}
final case class TestSuite()
final case class TestSuites() {
  val testSuites = new LinkedList[TestSuite]
}

trait RestService {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  // database
  val guiApplications = GuiApplications()

  def addApplication(): Id = {
    val apps = guiApplications.applications
    apps.synchronized {
      apps.add(new GuiApplication)
      Id(apps.size() - 1)
    }
  }
  def getApplication(id: Id): Option[GuiApplication] = {
    val apps = guiApplications.applications
    apps.synchronized {
      val index = id.id
      if (index >= 0 && index < apps.size()) Some(apps.get(index)) else None
    }
  }

  def getTestSuites(applicationId: Id): Option[TestSuites] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => Some(x.testSuites)
      case None => None
    }
  }

  def addTestSuite(applicationId: Id): Option[Id] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => {
        val testSuites = x.testSuites.testSuites
        testSuites.synchronized {
          testSuites.add(new TestSuite)
          Some(Id(testSuites.size() - 1))
        }
      }
      case None => None
    }
  }
  def getTestSuite(applicationId: Id, testSuiteId: Id): Option[TestSuite] = {
    val app = getApplication(applicationId)
    app match {
      case Some(x) => {
        val testSuites = x.testSuites.testSuites
        testSuites.synchronized {
          val index = testSuiteId.id
          if (index >= 0 && index < testSuites.size()) Some(testSuites.get(index)) else None
        }
      }
      case None => None
    }
  }

  // formats for unmarshalling and marshalling
  implicit val idFormat = jsonFormat1(Id)
  implicit val applicationFormat = jsonFormat0(GuiApplication)
  implicit val applicationsFormat = jsonFormat0(GuiApplications)
  implicit val testSuiteFormat = jsonFormat0(TestSuite)
  implicit val testSuitesFormat = jsonFormat0(TestSuites)

  val route: Route =
    get {
      path("applications") {
        complete(guiApplications)
      } ~
        pathPrefix("application" / IntNumber) { id =>
          val app = getApplication(Id(id))
          app match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        pathPrefix("application" / IntNumber / "test-suites") { id =>
          val testSuites = getTestSuites(Id(id))
          testSuites match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        pathPrefix("application" / IntNumber / "test-suite" / IntNumber) { (appId, suiteId) =>
          val suite = getTestSuite(Id(appId), Id(suiteId))
          suite match {
            case Some(x) => complete(x)
            case None => complete(StatusCodes.NotFound)
          }
        }
    } ~
      post {
        path("create-application") {
          val id = addApplication()
          complete(id)
        } ~
          pathPrefix("application" / IntNumber / "create-test-suite") { appId =>
            {
              val id = addTestSuite(Id(appId))
              complete(id)
            }
          }
      }
}