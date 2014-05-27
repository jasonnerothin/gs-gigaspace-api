package com.gigaspaces.sbp;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 5/26/14
 * Time: 2:52 PM
 */
public interface TransactionMaker {

    /**
     * Do something transactional, but don't return right away.
     * @param routeId on which partition we will be executing
     * @param spaceId id for the thing to work on
     * @param howLongInMillis minimum amount of time it will take to complete this transaction
     */
    @Transactional(propagation=Propagation.REQUIRED)
    void longTransaction(Integer routeId, String spaceId, Integer howLongInMillis);

}
