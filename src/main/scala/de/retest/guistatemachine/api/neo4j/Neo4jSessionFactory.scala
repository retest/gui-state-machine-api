package de.retest.guistatemachine.api.neo4j

import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.{Session, SessionFactory}
import org.neo4j.ogm.transaction.Transaction

import scala.collection.concurrent.TrieMap

object Neo4jSessionFactory {
  private val sessionFactories = TrieMap[String, SessionFactory]()

  def getSessionFactoryEmbedded(uri: String): SessionFactory = sessionFactories.get(uri) match {
    case Some(sessionFactory) => sessionFactory
    case None                 =>
      // TODO #19 This must not overwrite an existing database! Actually, one should use one shared session factory but we distinguish between directories.
      val conf = new Configuration.Builder().uri(uri).build
      val sessionFactory = new SessionFactory(conf, this.getClass.getPackage.getName)
      sessionFactories += (uri -> sessionFactory)
      sessionFactory
  }

  def getSessionFactoryBolt(uri: String): SessionFactory = sessionFactories.get(uri) match {
    case Some(sessionFactory) => sessionFactory
    case None                 =>
      // TODO #19 Retrieve server and login information from some user-defined config.
      val conf = new Configuration.Builder()
        .uri("bolt://localhost:7687")
        .credentials("neo4j", "bla")
        .build()
      val sessionFactory = new SessionFactory(conf, this.getClass.getPackage.getName)
      sessionFactories += (uri -> sessionFactory)
      sessionFactory
  }

  def transaction[A](f: Session => A)(implicit uri: String): A = {
    // We have to create a session for every transaction since sessions are not thread-safe.
    val session = getSessionFactoryEmbedded(uri).openSession()
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
