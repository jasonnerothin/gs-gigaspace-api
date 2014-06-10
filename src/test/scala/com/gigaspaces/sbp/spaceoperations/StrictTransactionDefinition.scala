package com.gigaspaces.sbp.spaceoperations

import org.springframework.transaction.TransactionDefinition
import scala.util.Random

/** User: jason
  * Date: 5/27/14
  * Time: 6:15 PM
  */
class StrictTransactionDefinition(timeout: Int = 10, readOnly: Boolean = true) extends TransactionDefinition{

  val rand = new Random(System.currentTimeMillis())

  override def getPropagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED

  override def getName = rand.nextString(8)

  override def getIsolationLevel = TransactionDefinition.ISOLATION_REPEATABLE_READ

  override def getTimeout = timeout

  override def isReadOnly = readOnly

}
