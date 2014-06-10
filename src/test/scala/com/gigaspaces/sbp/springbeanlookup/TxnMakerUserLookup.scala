package com.gigaspaces.sbp.springbeanlookup

import org.springframework.context.support.ClassPathXmlApplicationContext
import com.gigaspaces.sbp.TxnMakerUserOnClientSide

/** User: jason
  * Date: 5/27/14
  * Time: 6:31 PM
  */
trait TxnMakerUserLookup {

  implicit val clientContext: ClassPathXmlApplicationContext

  def lookupTxnMakerUser(): TxnMakerUserOnClientSide = clientContext.getBean("txnMakerUserOnClientSide").asInstanceOf[TxnMakerUserOnClientSide]

}
