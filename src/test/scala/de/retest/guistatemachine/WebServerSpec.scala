package de.retest.guistatemachine

import org.scalatest.WordSpec
import org.scalatest.Matchers

class WebServerSpec extends WordSpec with Matchers {

  "The web server" should {
    "start and end successfully" in {
      WebServer.main(Array("--maxtime=1000"))
    }
  }
}