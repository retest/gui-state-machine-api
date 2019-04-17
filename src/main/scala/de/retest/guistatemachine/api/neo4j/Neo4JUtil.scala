package de.retest.guistatemachine.api.neo4j

import org.neo4j.ogm.session.{Session, SessionFactory}
import org.neo4j.ogm.transaction.Transaction

object Neo4JUtil {

  def transaction[A](f: Session => A)(implicit sessionFactory: SessionFactory): A = {
    // We have to create a session for every transaction since sessions are not thread-safe.
    val session = sessionFactory.openSession()
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
