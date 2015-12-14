package com.caffinc.hermes.web

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase {
  implicit override val jsonFormats: Formats = DefaultFormats
}

class HermesSwagger extends Swagger("2.0", "0.1", new ApiInfo("Hermes API", "Hermes API", "", "", "", ""))