package com.caffinc.hermes.common.utils

import java.net.UnknownHostException

import com.mongodb.{DB, MongoClient, MongoClientURI}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

/**
  * Mongo Connection helper
  * @author Sriram
  */
object MongoConnector extends LazyLogging {
  private val MONGO_DEFAULT: String = ConfigFactory.load().getString("mongo.connection")
  private var clients = scala.collection.mutable.Map[String, MongoClient]()
  private val DB_MAP = scala.collection.mutable.Map[String, DB]()

  /**
    * Returns a MongoClient for a mongo URI
    * @param mongoConnection MongoConnection String
    * @return
    */
  def getClient(mongoConnection: String): Option[MongoClient] = {
    if (!clients.contains(mongoConnection)) {
      logger.info("Request for a new Client @ " + mongoConnection)
      clients synchronized {
        if (!clients.contains(mongoConnection)) {
          logger.info("Obtaining Client @ " + mongoConnection)
          try {
            clients += (mongoConnection -> new MongoClient(new MongoClientURI(mongoConnection)))
          } catch {
            case e: UnknownHostException =>
              logger.error("Could not connect to MongoDB", e)
          }
        }
      }
    }
    clients.get(mongoConnection)
  }

  /**
    * Returns the MongoClient for the URI picked from the config
    * @return
    */
  def getClient: Option[MongoClient] = {
    getClient(MONGO_DEFAULT)
  }

  /**
    * Returns the DB for the default MongoClient
    * @param dbName Database name to fetch
    * @return
    */
  def getDB(dbName: String): Option[DB] = {
    if (!DB_MAP.contains(dbName)) {
      logger.info("Request for a new DB @ " + dbName)
      DB_MAP synchronized {
        if (!DB_MAP.contains(dbName)) {
          logger.info("Obtaining DB @ " + dbName)
          getClient match {
            case Some(client) =>
              DB_MAP += (dbName -> client.getDB(dbName))
            case None =>
              // Do nothing
          }
        }
      }
    }
    DB_MAP.get(dbName)
  }
}
