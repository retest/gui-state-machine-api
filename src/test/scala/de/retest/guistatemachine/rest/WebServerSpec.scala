package de.retest.guistatemachine.rest

import org.scalatest.{Matchers, WordSpec}

class WebServerSpec extends WordSpec with Matchers {

  "The web server" should {
    "start and end successfully" in {
      WebServer.Host shouldEqual "localhost"
      WebServer.Port shouldEqual 8888
      WebServer.main(Array("--maxtime=1000"))
    }
  }
}
