package com.caffinc.hermes.web.servlets

import org.scalatra.ScalatraServlet

/**
 * Hermes Home Page
 */
class HermesHome extends ScalatraServlet
{
  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
}
