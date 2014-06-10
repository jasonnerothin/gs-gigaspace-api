package com.gigaspaces.sbp.gstest

import org.scalatest.ConfigMap

/** User: jason
  * Date: 5/27/14
  * Time: 8:47 PM
  */
trait GetFromConfigMap {

  val schemaProperty = "schema"
  val spaceUrlProperty = "spaceUrl"
  val numInstancesProperty = "numInstances"
  val numBackupsProperty = "numBackups"
  val instanceIdProperty = "instanceId"
  val spaceModeProperty = "spaceMode"
  val configLocationProperty = "configLocation"
  val localViewQueryListProperty = "localViewQueryList"

  implicit val defaultConfigMap: ConfigMap

  def getProperty(propertyName: String, configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Any = {
    val prop = configMap.get(propertyName)
    val innerP = prop match {
      case (Some(p)) => p
      case _ =>
        defaultConfigMap.get(propertyName)
    }
    innerP
  }

}