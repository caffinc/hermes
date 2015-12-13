package com.caffinc.hermes.email.entities

import scala.util.Try

/**
  * Holds the contents of an email
  * @author Sriram
  */
case class EmailParameters(
                            from: String,
                            password: String,
                            to: List[String],
                            subject: String,
                            content: String,
                            attachments: Option[List[String]] = None) {
  def validate: Try[EmailParameters] = {
    Try {
      new EmailParameters(
        this.from match {
          case null =>
            throw new Exception("No \"from\" field")
          case `from` =>
            from
        },
        this.password match {
          case null =>
            throw new Exception("No \"password\" field")
          case `password` =>
            password
        },
        this.to match {
          case null =>
            throw new Exception("No \"to\" field")
          case `to` =>
            to
        },
        this.subject match {
          case null =>
            ""
          case `subject` =>
            subject
        },
        this.content match {
          case null =>
            ""
          case `content` =>
            content
        },
        this.attachments
      )
    }
  }
}
