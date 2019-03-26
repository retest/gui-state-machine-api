package de.retest.guistatemachine.api.neo4j

import org.neo4j.ogm.session.SessionFactory

// TODO #19 Use sessions to modify the state graph.
object Neo4jSessionFactory {
  private val sessionFactory = new SessionFactory("de.retest.guistatemachine.api.neo4j")

  def getSession() = sessionFactory.openSession()
}
