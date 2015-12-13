package com.caffinc.hermes.email

import java.io.File
import java.util.Properties
import javax.activation.{DataHandler, DataSource, FileDataSource}
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}

import com.caffinc.hermes.email.entities.EmailParameters
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

/**
  * Sends emails via remote SMTP server
  * @author Sriram
  */
class Emailer(mailProperties: Option[Properties] = None) extends LazyLogging {
  private val _mailProperties: Properties = mailProperties match {
    case Some(property) =>
      property
    case None =>
      val mailProperties = new Properties
      mailProperties.put("mail.smtp.host", "smtp.gmail.com")
      mailProperties.put("mail.smtp.starttls.enable", "true")
      mailProperties.put("mail.smtp.auth", "true")
      mailProperties.put("mail.transport.protocol", "smtp")
      mailProperties.put("mail.smtp.port", "587")
      logger.info("Default properties {}", mailProperties)
      mailProperties
  }

  private def session(userName: String, password: String): Session = Session.getInstance(_mailProperties, new Authenticator() {
    protected override def getPasswordAuthentication: PasswordAuthentication = {
      new PasswordAuthentication(userName, password)
    }
  })

  /**
    * Adds an attachment to the given multipar
    * @param multipart the multipart file
    * @param file the file to attach
    */
  private def addAttachment(multipart: Multipart, file: File) {
    val source: DataSource = new FileDataSource(file.getAbsolutePath)
    val messageBodyPart: BodyPart = new MimeBodyPart
    messageBodyPart.setDataHandler(new DataHandler(source))
    messageBodyPart.setFileName(file.getName)
    multipart.addBodyPart(messageBodyPart)
  }

  /**
    * Sends a mail
    * @param params the params which describes the mail
    */
  def sendMail(params: EmailParameters): Try[Unit] = {
    params.validate match {
      case Success(`params`) =>
        Try {
          val message: MimeMessage = new MimeMessage(session(params.from, params.password))
          message.setFrom(new InternetAddress(params.from))
          params.to.foreach { to =>
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
          }
          message.setSubject(params.subject)

          params.attachments == null || params.attachments.isEmpty match {
            case true =>
              message.setContent(params.content, "text/plain")

            case false =>
              val messageBodyPart: BodyPart = new MimeBodyPart
              messageBodyPart.setText(params.content)
              val multipart: Multipart = new MimeMultipart
              multipart.addBodyPart(messageBodyPart)

              params.attachments.foreach { path =>
                val file: File = new File(path)
                if (!file.exists) {
                  throw new Exception("File " + path + " does not exist")
                }
                if (file.isDirectory) {
                  throw new Exception("File " + path + " is a directory")
                }
                addAttachment(multipart, file)
              }
              message.setContent(multipart)
          }

          logger.info("Attempting to send mail with params:")
          logger.info("  From: {}", params.from)
          logger.info("  To: {}", params.to)
          logger.info("  Subject: {}", params.subject)
          logger.info("  Content: {}", params.content)
          params.attachments match {
            case null =>
            // Do nothing
            case attachments =>
              logger.info("  Attachments: {}", attachments)
          }
          Transport.send(message)
          logger.info("Mail sent successfully!")
        }
      case Failure(e) =>
        Failure(e)
    }
  }
}
