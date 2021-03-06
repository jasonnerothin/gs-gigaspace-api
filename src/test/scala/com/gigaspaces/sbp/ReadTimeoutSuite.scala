package com.gigaspaces.sbp

import org.slf4j.{Logger, LoggerFactory}

import org.springframework.transaction.TransactionStatus
import scala.concurrent.ExecutionContext
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits
import com.gigaspaces.sbp.springbeanlookup.{TxnMakerUserLookup, TxnMgrLookup}
import com.gigaspaces.sbp.spaceoperations.{StrictTransactionDefinition, UpdatesSpaceThing, ReadsSpaceThingById}
import com.gigaspaces.sbp.datageneration.GeneratesSpaceThings
import com.gigaspaces.sbp.gstest.{GigaSpaceCreation, EmbeddedSinglePartitionSuite, GetFromConfigMap}

/** User: jason
  * Date: 4/27/14
  * Time: 5:30 AM
  * NOTE: In order for this test to run with two partitions, an appropriate gslicense.xml file
  * should be installed at src/test/resources/gslicense.xml
  */
class ReadTimeoutSuite extends EmbeddedSinglePartitionSuite
  with GetFromConfigMap
  with GigaSpaceCreation
  with TxnMgrLookup
  with TxnMakerUserLookup
  with ReadsSpaceThingById
  with UpdatesSpaceThing
  with GeneratesSpaceThings {

  val logger: Logger = LoggerFactory.getLogger(getClass)
  val txnMakerUser = lookupTxnMakerUser()
  val rand = new Random(System.currentTimeMillis())
  val gigaSpace = createGigaSpace(configMap = defaultConfigMap)

  val executionContext: ExecutionContext = Implicits.global

  test("readById timeout returns null") {

    // write a test thing and start a transaction
    val testPayload = "something-something"
    val routeId = 0
    val testThing: SpaceThing = generateTestSpaceThing(testPayload, routeId = routeId)
    val spaceId = gigaSpace.write(testThing).getUID

    val txn:TransactionStatus = txnMgr.getTransaction(new StrictTransactionDefinition(timeout = 100))

    doRead(spaceId) // timeout = 1 ms
    doRead(spaceId)
    doRead(spaceId)
    doUpdate(routeId, spaceId, timeout = 50)
    doRead(spaceId)
    doRead(spaceId)
    doRead(spaceId)

    txnMgr.rollback(txn)

  }

}