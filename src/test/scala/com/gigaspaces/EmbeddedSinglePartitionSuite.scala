package com.gigaspaces

import scala.util.Random
import com.j_spaces.core.client.SQLQuery
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.scalatest.ShouldMatchers

/** User: jason
  * Date: 5/27/14
  * Time: 6:10 PM
  */
abstract class EmbeddedSinglePartitionSuite extends GsI10nSuite
  with BeforeAndAfterAllConfigMap with ShouldMatchers {

  val spaceName = getClass.getSimpleName

  defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(numPartitions)
    , numBackupsProperty -> int2Integer(0)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> s"jini:/*/*/ReadTimeoutSuite?groups=gigaspaceApi"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> s"classpath:${spaceName}Server.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  lazy val clientContext: ClassPathXmlApplicationContext = loadContext()

  val numPartitions = 1
  val defaultConfigMap = new ConfigMap(defaults)
  val gigaSpace = createGigaSpace(configMap = defaultConfigMap)

  // SETUP METHODS
  override def beforeAll(cm: ConfigMap): Unit = {
    setupWith(defaultConfigMap)
  }

  // HELPER METHODS
  def loadContext(descriptor: String = s"classpath:${spaceName}Client.xml"): ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

}