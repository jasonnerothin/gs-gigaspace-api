package com.gigaspaces.sbp.gstest

import com.j_spaces.core.client.SQLQuery
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.scalatest.ShouldMatchers
import org.openspaces.core.GigaSpace
import org.slf4j.Logger

/** User: jason
  * Date: 5/27/14
  * Time: 6:10 PM
  */
abstract class EmbeddedSinglePartitionSuite
  extends GsI10nSuite
  with BeforeAndAfterAllConfigMap
  with ShouldMatchers {

  lazy val clientContext: ClassPathXmlApplicationContext = loadContext()

  implicit val gigaSpace: GigaSpace
  implicit val logger: Logger

  val spaceName = getClass.getSimpleName
  val numPartitions = 1
  val defaultsMap = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(numPartitions)
    , numBackupsProperty -> int2Integer(0)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> s"/./$spaceName"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> s"classpath:${spaceName}Client.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )
  val defaultConfigMap = new ConfigMap(defaultsMap)

  // SETUP METHODS
  override def beforeAll(cm: ConfigMap): Unit = {
    logger.debug("beforeAll in ESPS")
    logger.debug(s"SPACE NAME WILL BE: $spaceName")
    setupWith(defaultConfigMap)
  }

  // HELPER METHODS
  def loadContext(descriptor: String = s"classpath:${spaceName}Server.xml"): ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

  /* convenience methods */
  protected def spaceContents(): Int = {
    assume(gigaSpace != null)
    gigaSpace.count(new Object())
  }

}