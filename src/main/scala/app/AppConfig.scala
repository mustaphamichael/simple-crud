package app

import com.typesafe.config.ConfigFactory

/*
 * @created - 01/12/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait AppConfig {
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")

  lazy val interface: String = httpConfig.getString("host")
  lazy val port: Int = httpConfig.getInt("port")
}
