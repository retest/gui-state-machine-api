package de.retest.guistatemachine.rest

import org.scalatest.Matchers
import org.scalatest.WordSpec

class WebServerSpec extends WordSpec with Matchers {

  "The web server" should {
    "start and end successfully" in {
      WebServer.main(Array("--maxtime=1000"))
    }
  }
}