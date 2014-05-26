package com.gigaspaces.sbp;

import org.openspaces.remoting.ExecutorProxy;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 5/26/14
 * Time: 3:04 PM
 * Client-side wrapper for server-side invokation.
 */
@Component
public class TxnMakerUserOnClientSide {

    @ExecutorProxy
    private TransactionMaker transactionMaker;

    public void longTransaction(Integer routeId, String spaceId, Integer howLongInMillis) {
        transactionMaker.longTransaction(routeId, spaceId, howLongInMillis);
    }

//    public void setTransactionMaker(TransactionMaker transactionMaker) {
//        this.transactionMaker = transactionMaker;
//    }
}
