package de.retest.guistatemachine.api.neo4j

import com.typesafe.scalalogging.Logger
import org.neo4j.ogm.session.SessionFactory

import scala.collection.concurrent.TrieMap

sealed trait Neo4JSessionFactory

object Neo4JSessionFactory {
  private val logger = Logger[Neo4JSessionFactory]

  /**
    * Session factories should always be shared in the application. Besides, we have to avoid exceptions like:
    * ```
    * org.neo4j.kernel.StoreLockException: Unable to obtain lock on store lock file: /tmp/GuiStateMachineApiNeo4jSpec8181209634775261316/store_lock.
    * Please ensure no other process is using this database, and that the directory is writable (required even for read-only access)
    * ```
    * Apparently, Neo4J does not support multiple processes to access the same embedded database.
    * TODO #19 Can we allow access by multiple processes on the same database.
    * TODO #19 When do we close this?
    */
  private val sessionFactories = TrieMap[Neo4JConfig, SessionFactory]()

  def getSessionFactory(neo4JConfig: Neo4JConfig): SessionFactory = sessionFactories.get(neo4JConfig) match {
    case Some(sessionFactory) =>
      logger.info("Reusing session factory for {}", neo4JConfig)
      sessionFactory
    case None =>
      logger.info("Creating new session factory for {}", neo4JConfig)
      val conf = neo4JConfig.buildConfig()
      val packageName = this.getClass.getPackage.getName
      val sessionFactory = new SessionFactory(conf, packageName)
      sessionFactories += (neo4JConfig -> sessionFactory)
      sessionFactory

  }

  def removeSessionFactory(neo4JConfig: Neo4JConfig): Boolean = sessionFactories.remove(neo4JConfig) match {
    case Some(sessionFactory) =>
      logger.info("Closing session factory for {}", neo4JConfig)
      sessionFactory.close()
      true
    case None => false
  }
}
