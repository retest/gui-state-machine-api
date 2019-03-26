package de.retest.guistatemachine.api.neo4j

import de.retest.guistatemachine.api.AbstractApiSpec
import org.scalatest.BeforeAndAfterEach

class SutStateConverterSpec extends AbstractApiSpec with BeforeAndAfterEach {

  "SutStateConverter" should {
    "save and load SutState " in {
      val cut = new SutStateConverter
      val rootElementA = getRootElement("a", 0)
      val rootElementB = getRootElement("b", 0)
      val rootElementC = getRootElement("c", 0)
      val initialSutState = createSutState(rootElementA, rootElementB, rootElementC)

      val result = cut.toGraphProperty(initialSutState)
      result shouldEqual
        """<?xml version="1.0" encoding="UTF-8"?>
          |<sutState xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          |	<descriptors retestId="retestId" screenId="0" screen="screen0" title="My Window">
          |		<identifyingAttributes>
          |			<attributes>
          |				<attribute key="a" xsi:type="stringAttribute">a</attribute>
          |				<attribute key="b" xsi:type="stringAttribute">a</attribute>
          |				<attribute key="c" xsi:type="stringAttribute">a</attribute>
          |			</attributes>
          |		</identifyingAttributes>
          |		<attributes/>
          |		<screenshot>
          |			<persistenceId>prefix_039058c6f2c0cb492c533b0a4d14ef77cc0f78abccced5287d84a1a2011cfb81</persistenceId>
          |			<type>PNG</type>
          |		</screenshot>
          |	</descriptors>
          |	<descriptors retestId="retestId" screenId="0" screen="screen0" title="My Window">
          |		<identifyingAttributes>
          |			<attributes>
          |				<attribute key="a" xsi:type="stringAttribute">b</attribute>
          |				<attribute key="b" xsi:type="stringAttribute">b</attribute>
          |				<attribute key="c" xsi:type="stringAttribute">b</attribute>
          |			</attributes>
          |		</identifyingAttributes>
          |		<attributes/>
          |		<screenshot>
          |			<persistenceId>prefix_039058c6f2c0cb492c533b0a4d14ef77cc0f78abccced5287d84a1a2011cfb81</persistenceId>
          |			<type>PNG</type>
          |		</screenshot>
          |	</descriptors>
          |	<descriptors retestId="retestId" screenId="0" screen="screen0" title="My Window">
          |		<identifyingAttributes>
          |			<attributes>
          |				<attribute key="a" xsi:type="stringAttribute">c</attribute>
          |				<attribute key="b" xsi:type="stringAttribute">c</attribute>
          |				<attribute key="c" xsi:type="stringAttribute">c</attribute>
          |			</attributes>
          |		</identifyingAttributes>
          |		<attributes/>
          |		<screenshot>
          |			<persistenceId>prefix_039058c6f2c0cb492c533b0a4d14ef77cc0f78abccced5287d84a1a2011cfb81</persistenceId>
          |			<type>PNG</type>
          |		</screenshot>
          |	</descriptors>
          |</sutState>
          |""".stripMargin

      val loadedSutState = cut.toEntityAttribute(result)
      loadedSutState shouldEqual initialSutState
    }
  }
}
