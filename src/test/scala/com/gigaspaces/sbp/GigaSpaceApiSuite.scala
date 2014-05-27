package com.gigaspaces.sbp

import com.gigaspaces.GsI10nSuite
import scala.util.{Success, Failure, Random}
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import com.j_spaces.core.client.SQLQuery
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.slf4j.{Logger, LoggerFactory}
import org.specs2.matcher.ShouldMatchers
import scala.concurrent.{Promise, ExecutionContext, Future}
import concurrent.ExecutionContext.Implicits.global

import scala.concurrent.ExecutionContext.Implicits.global
import net.jini.core.transaction.TimeoutExpiredException
import org.openspaces.core.ChangeException
import org.springframework.transaction.{TransactionStatus, TransactionDefinition, PlatformTransactionManager}
import org.springframework.transaction.support.DefaultTransactionDefinition

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 4/27/14
  * Time: 5:30 AM
  * NOTE: In order for this test to run with two partitions, an appropriate gslicense.xml file
  * should be installed at src/test/resources/gslicense.xml
  */
class GigaSpaceApiSuite extends GsI10nSuite with ShouldMatchers with BeforeAndAfterAllConfigMap {

  val logger: Logger = LoggerFactory.getLogger(getClass)

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

  override def afterAll(cm: ConfigMap): Unit = {
  }


  // TESTS

  lazy val txnMakerUser = clientContext.getBean("txnMakerUserOnClientSide").asInstanceOf[TxnMakerUserOnClientSide]

  test("test read timeout throws") {

    // write a test thing and start a transaction
    val testPayload = "something-something"
    val routeId = 0
    val testThing: SpaceThing = generateTestSpaceThing(testPayload, routeId = routeId)
    val spaceId = gigaSpace.write(testThing).getUID

    val txn:TransactionStatus = txnMgr.getTransaction(new TxnDef(timeout = 100))

    doRead(spaceId) // timeout = 1 ms
    doRead(spaceId)
    doRead(spaceId)
    doUpdate(routeId, spaceId, timeout = 50)
    doRead(spaceId)
    doRead(spaceId)
    doRead(spaceId)

    txnMgr.rollback(txn)

  }

  class TxnDef(timeout: Int = 10, readOnly: Boolean = true) extends TransactionDefinition{

    override def getPropagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED

    override def getName = rand.nextString(8)

    override def getIsolationLevel = TransactionDefinition.ISOLATION_REPEATABLE_READ

    override def getTimeout = timeout

    override def isReadOnly = readOnly
  }

  def doRead(spaceId: String): Future[SpaceThing] = {
    val read = Future {
      logger.trace("Read submitted.")
      gigaSpace.readById(classOf[SpaceThing], spaceId, 1)
    }
    read.onComplete {
      case Success(d) => logger.trace("Read complete.")
      case Failure(e) => logger.error("Error during read", e); throw e
    }
    read
  }

  def doUpdate(routeId: Int, spaceId: String, timeout: Int = 1500): Future[Unit] = {
    val update = Future {
      logger.trace("Update submitted.")
      txnMakerUser.longTransaction(routeId, spaceId, timeout)
    }
    update.onComplete {
      case Success(s) => logger.trace("Update complete.")
      case Failure(swallowMe: ChangeException) => logger.debug("|XXX> Update [timeout] <XXX|")
      case Failure(e) => logger.error("!!! Update failure! !!!"); throw e
    }
    update
  }

  // HELPER METHODS

  def loadContext(descriptor: String = s"classpath:${spaceName}Client.xml"): ClassPathXmlApplicationContext = {
    new ClassPathXmlApplicationContext(descriptor)
  }

  def txnMgr : PlatformTransactionManager = {
    clientContext.getBean("transactionManager").asInstanceOf[PlatformTransactionManager]
  }


  // TEST DATA GENERATION

  def generateTestSpaceThing(payload: String = rand.nextString(8),
                             routeId: Int = rand.nextInt(numPartitions),
                             spaceId: java.lang.String = null): SpaceThing = {

    val testThing = new SpaceThing
    testThing.setPayload(payload)
    testThing.setRouteId(routeId)
    testThing.setSpaceId(spaceId) // auto-generated (hence null default var)
    testThing
  }

}