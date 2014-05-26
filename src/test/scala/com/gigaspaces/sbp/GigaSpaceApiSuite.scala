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
import com.gigaspaces.client.ChangeSet
import com.gigaspaces.async.AsyncFutureListener
import com.gigaspaces.query.IdQuery
import org.springframework.transaction.annotation.Transactional
import scala.util.control.NonFatal

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 4/27/14
  * Time: 5:30 AM
  * NOTE: In order for this test to run with two partitions, an appropriate gslicense.xml file
  * should be installed at src/test/resources/gslicense.xml
  */
class GigaSpaceApiSuite extends GsI10nSuite with ShouldMatchers{

  val logger:Logger = LoggerFactory.getLogger(getClass)

  // SETUP STUFF

  val rand = new Random(System.currentTimeMillis())
  val numPartitions = 1
  val spaceName = classOf[GigaSpaceApiSuite].getSimpleName

  defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(numPartitions)
    , numBackupsProperty -> int2Integer(0)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> s"jini:/*/*/$spaceName?groups=gigaspaceApi"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> s"classpath:$spaceName.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  val defaultConfigMap = new ConfigMap(defaults)

  // fields initialized for test
  var testThings: Seq[SpaceThing] = null

  // initialization methods

  override def beforeAll(cm: ConfigMap): Unit = {

    setupWith(defaultConfigMap)

  }

  override def beforeEach(): Unit = {

  }


  def loadContext(descriptor: String) : ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

  // TESTS

  test("test read timeout throws ") {

    // write a test thing and start a transaction
    val testPayload = "something-something"
    val routeId = 0
    val testThing:SpaceThing = generateTestSpaceThing(testPayload, routeId = routeId)
    val spaceId = gigaSpace.write(testThing).getUID.asInstanceOf[java.lang.String]

    longTransaction(routeId, spaceId, howLongInMillis = 50)
    gigaSpace.readById(classOf[SpaceThing], spaceId, 1)

  }

  // TEST DATA GENERATION

  @Transactional
  def longTransaction(routeId: Int, spaceId: java.lang.String, howLongInMillis: Int = 50 ) {

    val idQuery = new IdQuery[SpaceThing](classOf[SpaceThing], spaceId, routeId)
    val changeSet = new ChangeSet().set("payload", "laDeeDah")
    val result = gigaSpace.change(idQuery, changeSet)

    try{
      Thread.sleep(howLongInMillis)
    } catch{
      case e: InterruptedException => Thread.currentThread().interrupt()
      case NonFatal(e) => throw new IllegalStateException(e)
    }

  }

  def generateTestSpaceThing(payload: String = rand.nextString(8),
                             routeId: Int = rand.nextInt(numPartitions),
                             spaceId: java.lang.String = null ) : SpaceThing = {

    val testThing = new SpaceThing
    testThing.setPayload(payload)
    testThing.setRouteId(routeId)
    testThing.setSpaceId(spaceId) // auto-generated (hence null default var)
    testThing
  }

}