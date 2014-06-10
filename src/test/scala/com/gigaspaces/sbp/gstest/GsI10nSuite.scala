/*
 * Copyright [2014] [Jason Nerothin]
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.gigaspaces.sbp.gstest

import org.scalatest._
import org.openspaces.core.cluster.ClusterInfo
import org.openspaces.pu.container.ProcessingUnitContainer
import org.openspaces.pu.container.integrated.IntegratedProcessingUnitContainerProvider

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/27/14
  * Time: 3:25 PM
  *
  * An abstract test suite that can be used to instrument scala tests that start up a
  * new container in then create a [[org.openspaces.core.GigaSpace]] reference into it.
  */
abstract class GsI10nSuite
  extends FunSuite
  with GetFromConfigMap
  with BeforeAndAfterAllConfigMap {

  /**
   * Test instances. The purpose of this class is to initialize these members
   */
  protected var container: ProcessingUnitContainer = null

  /* Default setup/tear-down behaviors */

  protected def setupWith(configMap: ConfigMap): Unit = {
    container = createContainer(configMap)
  }

  override def afterAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    container.close()
  }

  /* i10n infrastructure setup methods */

  private def createClusterInfo(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): ClusterInfo = {

    val schema = getProperty(schemaProperty, configMap)
    val numInstances = getProperty(numInstancesProperty, configMap)
    val numBackups = getProperty(numBackupsProperty, configMap)
    val instanceId = getProperty(instanceIdProperty, configMap)

    // not type-safe, but don't care
    val clusterInfo = new ClusterInfo
    clusterInfo.setSchema(schema.asInstanceOf[String])
    clusterInfo.setNumberOfInstances(numInstances.asInstanceOf[Integer])
    clusterInfo.setNumberOfBackups(numBackups.asInstanceOf[Integer])
    clusterInfo.setInstanceId(instanceId.asInstanceOf[Integer])
    clusterInfo

  }

  private def createContainer(configMap: ConfigMap): ProcessingUnitContainer = {
    val containerProvider = new IntegratedProcessingUnitContainerProvider
    containerProvider.setClusterInfo(createClusterInfo(configMap))
    val loc = getProperty(configLocationProperty, configMap).asInstanceOf[String]
    containerProvider.addConfigLocation(loc)
    val container:ProcessingUnitContainer = containerProvider.createContainer()
    container
  }

}