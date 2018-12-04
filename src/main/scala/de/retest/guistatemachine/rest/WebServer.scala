package de.retest.guistatemachine.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import de.retest.guistatemachine.api.impl.GuiStateMachineApiImpl
import scopt.OptionParser

import scala.io.StdIn

object WebServer extends App with RestService {
  final val Host = "localhost"
  final val Port = 8888

  implicit val system = ActorSystem("gui-state-machine-api-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  case class Config(maxtime: Long = -1)

  val parser = new OptionParser[Config]("gui-state-machine") {
    head("gui-state-machine-api", "1.0")

    opt[Long]('t', "maxtime") action { (x, c) =>
      c.copy(maxtime = x)
    } text ("maxtime specifies the maximum up time for the HTTP server in ms. This option can be helpful to run tests over a specific period of time.")

    help("help").text("prints this usage text")
  }
  // parser.parse returns Option[C]
  parser.parse(args, Config()) match {
    case Some(config) =>
      val bindingFuture = Http().bindAndHandle(getRoute(GuiStateMachineApiImpl), Host, Port)

      println(s"Server online at http://${Host}:${Port}/")

      if (config.maxtime < 0) {
        println("Press RETURN to stop...")
        StdIn.readLine() // let it run until user presses return
      } else {
        println(s"The server will shutdown automatically after ${config.maxtime / 1000} seconds.")
        Thread.sleep(config.maxtime)
      }
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    case None => println("Missing config.")
  }
}
