package de.retest.guistatemachine.api.neo4j
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import de.retest.recheck.XmlTransformerUtil
import de.retest.recheck.ui.descriptors.SutState
import org.neo4j.ogm.typeconversion.AttributeConverter

/*
 * https://github.com/neo4j/neo4j-ogm/blob/master/neo4j-ogm-docs/src/main/asciidoc/reference/conversion.adoc
 */
class SutStateConverter extends AttributeConverter[SutState, String] {
  def toGraphProperty(value: SutState): String = XmlTransformerUtil.getXmlTransformer.toXML(value)
  def toEntityAttribute(value: String): SutState =
    XmlTransformerUtil.getXmlTransformer.fromXML[SutState](new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)))
}
