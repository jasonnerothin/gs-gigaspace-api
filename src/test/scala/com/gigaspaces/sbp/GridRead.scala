package com.gigaspaces.sbp

import org.openspaces.core.{ChangeException, GigaSpace}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import org.slf4j.Logger

/** User: jason
  * Date: 5/27/14
  * Time: 6:23 PM
  */
trait GridRead {

  implicit val logger: Logger
  implicit val gigaSpace: GigaSpace
  implicit val executionContext: ExecutionContext

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

}
