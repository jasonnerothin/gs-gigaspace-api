package com.gigaspaces.sbp.gstest

import org.scalatest.ConfigMap
import org.openspaces.core.{GigaSpaceConfigurer, GigaSpace}
import org.openspaces.core.space.UrlSpaceConfigurer
import org.openspaces.core.space.cache.{LocalViewSpaceConfigurer, LocalCacheSpaceConfigurer}
import com.j_spaces.core.client.SQLQuery

/**
  * Created by IntelliJ IDEA.
  * User: jason
  * Date: 5/27/14
  * Time: 8:41 PM
  * Provides a mechanism for creating a [[GigaSpace]] reference from a
  * [[ConfigMap]].
  */
trait GigaSpaceCreation extends GetFromConfigMap{

  import SpaceMode._

  def createGigaSpace(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): GigaSpace = {

    def makeGs(configurer: UrlSpaceConfigurer): GigaSpace = {
      new GigaSpaceConfigurer(configurer).gigaSpace()
    }

    val spaceUrl = getProperty(spaceUrlProperty, configMap).asInstanceOf[String]
    val configurer = new UrlSpaceConfigurer(spaceUrl)

    getProperty(spaceModeProperty, configMap) match {
      case Embedded =>
        makeGs(configurer)
      case Remote =>
        makeGs(configurer)
      case LocalCache =>
        new GigaSpaceConfigurer(new LocalCacheSpaceConfigurer(configurer)).gigaSpace()
      case LocalView =>
        val queries = getProperty(localViewQueryListProperty, configMap).asInstanceOf[List[SQLQuery[_]]]
        val viewConfigurer = new LocalViewSpaceConfigurer(configurer)
        queries.foreach(qry => {
          viewConfigurer.addViewQuery(qry)
        })
        new GigaSpaceConfigurer(viewConfigurer).gigaSpace()
    }

  }

}