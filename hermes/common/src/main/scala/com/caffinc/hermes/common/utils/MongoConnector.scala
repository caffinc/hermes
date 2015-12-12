package com.caffinc.hermes.common.utils

import com.mongodb.{DB, MongoClient, MongoClientURI}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

/**
  * Mongo Connection helper
  * @author Sriram
  */
object MongoConnector extends LazyLogging {
  private val MONGO_DEFAULT: String = ConfigFactory.load().getString("mongo.connection")
  private val CLIENT_MAP: java.util.Map[String, MongoClient] = new java.util.HashMap[String, MongoClient]
  private val DB_MAP: java.util.Map[String, DB] = new java.util.HashMap[String, DB]

  /**
    * Returns a MongoClient for a mongo URI
    * @param mongoConnection MongoConnection String
    * @return
    */
  def getClient(mongoConnection: String): MongoClient = {
    if (!CLIENT_MAP.containsKey(mongoConnection)) {
      logger.info("Request for a new Client @ " + mongoConnection)
      CLIENT_MAP synchronized {
        if (!CLIENT_MAP.containsKey(mongoConnection)) {
          logger.info("Obtaining Client @ " + mongoConnection)
          CLIENT_MAP.put(mongoConnection, new MongoClient(new MongoClientURI(mongoConnection)))
        }
      }
    }
    CLIENT_MAP.get(mongoConnection)
  }

  /**
    * Returns the MongoClient for the URI picked from the config
    * @return
    */
  def getClient: MongoClient = {
    getClient(MONGO_DEFAULT)
  }

  /**
    * Returns the DB for the default MongoClient
    * @param dbName Database name to fetch
    * @return
    */
  def getDB(dbName: String): DB = {
    if (!DB_MAP.containsKey(dbName)) {
      logger.info("Request for a new DB @ " + dbName)
      DB_MAP synchronized {
        if (!DB_MAP.containsKey(dbName)) {
          logger.info("Obtaining DB @ " + dbName)
          DB_MAP.put(dbName, getClient.getDB(dbName))
        }
      }
    }
    DB_MAP.get(dbName)
  }
}
