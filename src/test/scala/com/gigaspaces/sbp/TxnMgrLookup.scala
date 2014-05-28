package com.gigaspaces.sbp

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.context.support.ClassPathXmlApplicationContext

/** User: jason
  * Date: 5/27/14
  * Time: 6:29 PM
  */
trait TxnMgrLookup {

  implicit val clientContext: ClassPathXmlApplicationContext

  def txnMgr : PlatformTransactionManager = {
    clientContext.getBean("transactionManager").asInstanceOf[PlatformTransactionManager]
  }

}