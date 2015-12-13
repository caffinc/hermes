package com.caffinc.hermes.app

import com.beust.jcommander.{JCommander, Parameter}
import com.caffinc.hermes.common.utils.MongoConnector
import com.caffinc.hermes.email.Emailer
import com.caffinc.hermes.email.entities.EmailParameters
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

/**
  * Hermes Application
  * @author Sriram
  */
object App extends App with LazyLogging {

  object Args {
    @Parameter(
      names = Array("-u", "--user"),
      description = "Gmail username",
      required = false)
    var user: String = null

    @Parameter(
      names = Array("-p", "--pass"),
      description = "Gmail password",
      required = false
    )
    var password: String = null
  }

  println("Hermes v0.1")
  println("Personalized Corporate Employee Emailer")
  new JCommander(Args, args.toArray: _*)
  println(Args.user)
  println(Args.password)
  println(MongoConnector.getDB("hermes") match {
    case Some(db) =>
      db.getCollection("credentials").findOne
    case None =>
      "WUT"
  })
  new Emailer().sendMail(new EmailParameters(Args.user, Args.password, List("sriram@raremile.com"),
    "Test Scala Emailer!", "Let's see if I can do this in Scala!")) match {
    case Success(_) =>
    // Do nothing
    case Failure(e) =>
      logger.error("Couldn't send emails", e)
  }
}
