package com.gigaspaces.sbp

import org.springframework.transaction.annotation.{Isolation, Propagation, Transactional}
import com.gigaspaces.query.IdQuery
import org.openspaces.remoting.RemotingService
import com.gigaspaces.client.ChangeSet
import javax.annotation.Resource
import org.openspaces.core.GigaSpace
import scala.concurrent._
import ExecutionContext.Implicits.global
import org.slf4j.{LoggerFactory, Logger}

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 5/26/14
 * Time: 2:53 PM
 */
@RemotingService
class TxnMakerServerSideService extends TransactionMaker {

  val logger:Logger = LoggerFactory.getLogger(getClass)

  @Resource
  var gigaSpace: GigaSpace = null

  /** Do something transactional, but don't return right away.
    * @param routeId on which partition we will be executing
    * @param spaceId id for the thing to work on
    * @param howLongInMillis minimum amount of time it will take to complete this transaction
    */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  def longTransaction(routeId: Integer, spaceId: java.lang.String, howLongInMillis: Integer = 50): Unit = {

    logger.trace("long transaction received...")

    val idQuery = new IdQuery[SpaceThing](classOf[SpaceThing], spaceId, routeId)
    val changeSet = new ChangeSet().set("payload", "laDeeDah")
    gigaSpace.change(idQuery, changeSet)

    blocking {
      Thread.sleep(howLongInMillis.longValue())
    }

  }

}