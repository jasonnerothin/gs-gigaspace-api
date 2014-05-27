package com.gigaspaces.sbp

import com.gigaspaces.GsI10nSuite
import scala.util.Random
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import com.j_spaces.core.client.SQLQuery
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.matcher.ShouldMatchers
import scala.concurrent.{ExecutionContext, Future}
import concurrent.ExecutionContext.Implicits.global

import scala.concurrent.ExecutionContext.Implicits.global

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 4/27/14
  * Time: 5:30 AM
  * NOTE: In order for this test to run with two partitions, an appropriate gslicense.xml file
  * should be installed at src/test/resources/gslicense.xml
  */
class GigaSpaceApiSuite extends GsI10nSuite with ShouldMatchers with BeforeAndAfterAllConfigMap{

  val logger:Logger = LoggerFactory.getLogger(getClass)

  // SETUP CONFIG
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
    , configLocationProperty -> s"classpath:${spaceName}Server.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  val defaultConfigMap = new ConfigMap(defaults)
  lazy val clientContext: ClassPathXmlApplicationContext = loadContext()

  // SETUP METHODS
  override def beforeAll(cm: ConfigMap): Unit = {
    setupWith(defaultConfigMap)
  }

  override def beforeEach(): Unit = {
  }

  override def afterAll(cm: ConfigMap) : Unit = {
  }

  def loadContext(descriptor: String = s"classpath:${spaceName}Client.xml") : ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

  // TESTS

  lazy val txnMakerUser = clientContext.getBean("txnMakerUserOnClientSide").asInstanceOf[TxnMakerUserOnClientSide]

  test("test read timeout throws ") {

    // write a test thing and start a transaction
    val testPayload = "something-something"
    val routeId = 0
    val testThing:SpaceThing = generateTestSpaceThing(testPayload, routeId = routeId)
    val spaceId = gigaSpace.write(testThing).getUID

    val tm = txnMakerUser.asInstanceOf[TxnMakerUserOnClientSide].transactionMaker
    val tc = txnMakerUser.getClass

    logger.trace(s"$tm")
    logger.trace(s"$tc")

    val txn = Future {
      txnMakerUser.longTransaction(routeId, spaceId, 10)
      "done"
    }
    val read = Future{
      gigaSpace.readById(classOf[SpaceThing], spaceId, 1)
    }

    var failed = false

    val f = txn.evaluate
    val g = read.onFailure {
      case f: RuntimeException => failed = true
    }

    assert( failed === true )

  }

  // TEST DATA GENERATION

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