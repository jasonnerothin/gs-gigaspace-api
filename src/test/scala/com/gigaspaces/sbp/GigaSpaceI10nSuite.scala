package com.gigaspaces.sbp

import com.gigaspaces.GsI10nSuite
import scala.util.Random
import org.scalatest.ConfigMap
import com.j_spaces.core.client.SQLQuery
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.openspaces.core.GigaSpace
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.matcher.ShouldMatchers
import java.util

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 4/27/14
  * Time: 5:30 AM
  * NOTE: In order for this test to run with two partitions, an appropriate gslicense.xml file
  * should be installed at src/test/resources/gslicense.xml
  */
class GigaSpaceI10nSuite extends GsI10nSuite with ShouldMatchers{

  val logger:Logger = LoggerFactory.getLogger(getClass)

  // SETUP STUFF

  val rand = new Random(System.currentTimeMillis())
  val maxNumPartsPerWatch = 3
  val numPartitions = 2
  val numTestWatches = 4
  val spaceName = classOf[GigaSpaceI10nSuite].getSimpleName

  defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(numPartitions)
    , numBackupsProperty -> int2Integer(0)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> s"jini:/*/*/$spaceName?locators=localhost:4174&groups=watches"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> "classpath*:/META-INF/Spring/pu.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  val defaultConfigMap = new ConfigMap(defaults)
  val clientXmlContextResourceLocation = "classpath*:/META-INF/Spring/pu.xml"
  val remoteProxyBeanName = "brokenWatchOwner"
  val clusteredProxyBeanName = "gigaSpace"

  // fields initialized for test
  var testThings: Seq[SpaceThing] = null
  var clusteredProxy: GigaSpace = null

  // initialization methods

  override def beforeAll(cm: ConfigMap): Unit = {
    setupWith(defaultConfigMap)
    val ctxt = loadContext(clientXmlContextResourceLocation)

    clusteredProxy = ctxt.getBean(clusteredProxyBeanName).asInstanceOf[GigaSpace]
  }

  override def beforeEach(): Unit = {

  }

  def loadContext(descriptor: String) : ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

  // TESTS

  test("a test") {

  }

}