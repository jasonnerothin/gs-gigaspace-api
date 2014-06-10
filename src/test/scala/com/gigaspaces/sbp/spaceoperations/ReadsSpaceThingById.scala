package com.gigaspaces.sbp.spaceoperations

import org.openspaces.core.GigaSpace
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import org.slf4j.Logger
import com.gigaspaces.sbp.SpaceThing

/** User: jason
  * Date: 5/27/14
  * Time: 6:23 PM
  */
trait ReadsSpaceThingById {

  implicit val logger: Logger
  implicit val gigaSpace: GigaSpace
  implicit val executionContext: ExecutionContext

  def doRead(spaceId: String, readTimeout: Long = 1L): Future[SpaceThing] = {
    val read = Future {
      logger.trace("Read submitted.")
      gigaSpace.readById(classOf[SpaceThing], spaceId, readTimeout)
    }
    read.onComplete {
      case Success(d) => logger.trace("Read complete.")
      case Failure(e) => logger.error("Error during read", e); throw e
    }
    read
  }

}
