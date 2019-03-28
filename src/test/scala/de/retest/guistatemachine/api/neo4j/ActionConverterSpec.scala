package de.retest.guistatemachine.api.neo4j

import java.util

import de.retest.guistatemachine.api.AbstractApiSpec
import de.retest.surili.commons.actions._
import org.scalatest.BeforeAndAfterEach

class ActionConverterSpec extends AbstractApiSpec with BeforeAndAfterEach {
  private val rootElement = getRootElement("a", 0)
  private val sutState = createSutState(rootElement)
  private val cut = new ActionConverter(Some(sutState))

  "ActionConverter" should {

    "convert ChangeValueOfAction" in {
      val list = util.Arrays.asList("foo", "bar", "waa")
      val cs = list.toArray(new Array[CharSequence](list.size))
      val action = new ChangeValueOfAction(rootElement, cs)

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>ChangeValueOfAction</type><retestId>retestId</retestId><keys><sequence>foo</sequence><sequence>bar</sequence><sequence>waa</sequence></keys></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert ClickOnAction" in {
      val action = new ClickOnAction(rootElement)

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>ClickOnAction</type><retestId>retestId</retestId></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert NavigateToAction" in {
      val action = new NavigateToAction("http://google.com")

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>NavigateToAction</type><url>http://google.com</url></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert NavigateBackAction" in {
      val action = new NavigateBackAction()

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>NavigateBackAction</type></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert NavigateForwardAction" in {
      val action = new NavigateForwardAction()

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>NavigateForwardAction</type></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert NavigateRefreshAction" in {
      val action = new NavigateRefreshAction()

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>NavigateRefreshAction</type></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }

    "convert SwitchToWindowAction" in {
      val action = new SwitchToWindowAction("test")

      val result = cut.toGraphProperty(action)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<action><type>SwitchToWindowAction</type><window>test</window></action>
          |""".stripMargin

      val loadedAction = cut.toEntityAttribute(result)
      loadedAction shouldEqual action
    }
  }
}
