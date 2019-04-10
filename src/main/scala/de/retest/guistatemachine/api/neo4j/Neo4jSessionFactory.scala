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
      val sessionFactory = new SessionFactory(conf, this.getClass.getPackage.getName)
      sessionFactories += (uri -> sessionFactory)
      sessionFactory
  }

  def transaction[A](f: Session => A)(implicit uri: String): A = {
    // We have to create a session for every transaction since sessions are not thread-safe.
    val session = Neo4jSessionFactory.getSessionFactory(uri).openSession() // TODO #19 Close the session at some point?
    var txn: Option[Transaction] = None
    try {
      val transaction = session.beginTransaction()
      txn = Some(transaction)
      val r = f(session)
      transaction.commit()
      r
    } finally {
      txn match {
        case Some(transaction) => transaction.close()
        case None              =>
      }
    }
  }
}
