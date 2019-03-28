package de.retest.guistatemachine.api.neo4j

import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.{Session, SessionFactory}
import org.neo4j.ogm.transaction.Transaction

import scala.collection.concurrent.TrieMap

object Neo4jSessionFactory {
  private val sessionFactories = TrieMap[String, SessionFactory]()

  def getSessionFactory(uri: String): SessionFactory = sessionFactories.get(uri) match {
    case Some(sessionFactory) => sessionFactory
    case None =>
      val conf = new Configuration.Builder().uri(uri).build
      val sessionFactory = new SessionFactory(conf, "de.retest.guistatemachine.api.neo4j")
      sessionFactories += (uri -> sessionFactory)
      sessionFactory
  }

  def transaction[A](f: => A)(implicit session: Session): A = {
    var txn: Option[Transaction] = None
    try {
      txn = Some(session.beginTransaction())
      val r = f
      txn.get.commit()
      r
    } finally {
      if (txn.isDefined) { txn.get.close() }
    }
  }
}
