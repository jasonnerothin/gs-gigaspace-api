package com.gigaspaces.sbp

import org.springframework.context.support.ClassPathXmlApplicationContext

/** User: jason
  * Date: 5/27/14
  * Time: 6:31 PM
  */
trait TxnMakerUserLookup {

  implicit val clientContext: ClassPathXmlApplicationContext

  def lookupTxnMakerUser(): TxnMakerUserOnClientSide = clientContext.getBean("txnMakerUserOnClientSide").asInstanceOf[TxnMakerUserOnClientSide]

}
