package com.caffinc.hermes.common.utils

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.{URL, URLConnection}

import com.typesafe.scalalogging.LazyLogging

/**
  * Utility methods
  * @author Sriram
  */
object ApiUtils extends LazyLogging {
  /**
    * Returns the public IP by connecting to the ifconfig.co service
    * @return Public IPv4 IP of the caller, or 127.0.0.1 if the service returns nothing
    * @throws IOException Thrown if a connection to the remote server failed
    */
  def getPublicIP: String = {
    val maxAttempts: Int = 3
    for (attemptNumber <- 0 until maxAttempts) {
      try {
        val ifConfig: URLConnection = new URL("http://v4.ifconfig.co").openConnection
        ifConfig.setRequestProperty("user-agent", "curl/7.43.0")
        val in: BufferedReader = new BufferedReader(new InputStreamReader(ifConfig.getInputStream))
        var ip: String = null
        while ( {
          ip = in.readLine
          ip
        } != null) {
          if (!Utils.isNullOrWhitespace(ip)) {
            in.close()
            return ip
          }
        }
        in.close()
      }
      catch {
        case e: IOException =>
          logger.warn(s"Attempt #$attemptNumber: Could not fetch IP")
      }
    }
    logger.warn("Returning fallback IP 127.0.0.1")
    "127.0.0.1"
  }
}
