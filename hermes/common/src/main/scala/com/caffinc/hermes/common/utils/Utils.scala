package com.caffinc.hermes.common.utils

/**
  * Created by SriramKeerthi on 12/12/2015.
  */
object Utils {
  /**
    * String Util method
    * @param string the string
    * @return true if a string is null or an empty string
    */
  def isNullOrEmpty(string: String): Boolean = {
    string == null || string.isEmpty
  }

  /**
    * String Util method
    * @param string the string
    * @return true if a string is null or whitespace
    *         TODO Move this to StringUtils
    */
  def isNullOrWhitespace(string: String): Boolean = {
    string == null || string.trim.isEmpty
  }
}
