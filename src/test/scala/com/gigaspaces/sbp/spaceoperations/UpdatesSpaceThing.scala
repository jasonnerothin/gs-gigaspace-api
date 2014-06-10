package com.gigaspaces.sbp.spaceoperations

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import org.openspaces.core.ChangeException
import org.slf4j.Logger
import com.gigaspaces.sbp.TxnMakerUserOnClientSide

/** User: jason
  * Date: 5/27/14
  * Time: 6:25 PM
  */
trait UpdatesSpaceThing {

  implicit val txnMakerUser: TxnMakerUserOnClientSide
  implicit val logger: Logger
  implicit val executionContext: ExecutionContext

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

}
