package de.retest.guistatemachine

import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.retest.guistatemachine.persistence.Persistence

object WebServer extends App with RestService {
  final val HOST = "localhost"
  final val PORT = 8888

  implicit val system = ActorSystem("gui-state-machine-api-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val persistence = new Persistence

  val bindingFuture = Http().bindAndHandle(getRoute(persistence), HOST, PORT)

  println(s"Server online at http://${HOST}:${PORT}/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}