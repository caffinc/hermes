package com.caffinc.hermes.web.servlets

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}


/**
  * Handles User Management APIs
  * @author Sriram
  */
class UserManagement(implicit val swagger: Swagger) extends ScalatraServlet with NativeJsonSupport with SwaggerSupport {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  // The name of our application. This will show up in the Swagger docs.
  override protected val applicationName = Some("User Management API")

  // A description of our application. This will show up in the Swagger docs.
  protected val applicationDescription = "User Management API for Hermes. It exposes operations for signing in and managing users."

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  // An API description about retrieving flowers.
  val getMail =
    (apiOperation[List[String]]("getMail")
      summary "Get mail"
      notes "Notes for the getMail API"
      parameter queryParam[Option[String]]("name").description("Parameter called \"name\""))
  get("/mail", operation(getMail)) {
    "{\"msg\":\"Hello!\""
  }

}
