package org.atinject.core.transaction;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.infinispan.transaction.tm.DummyTransactionManager;

public class InfinispanTransactionManager
{
    public static UserTransaction getUserTransaction()
    {
        return DummyTransactionManager.getUserTransaction();
    }
    
    public static void begin() throws NotSupportedException, SystemException
    {
        getUserTransaction().begin();
    }
    
    public static void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException
    {
        getUserTransaction().commit();
    }
    
    public static void rollback() throws SystemException
    {
        getUserTransaction().rollback();
    }
    
    public static void setRollbackOnly() throws SystemException
    {
        getUserTransaction().setRollbackOnly();
    }
    
    public static int getUserTransactionStatus() throws SystemException
    {
        return getUserTransaction().getStatus();
    }
    
    public static boolean isStatusNoTransaction() throws SystemException
    {
        return getUserTransactionStatus() == Status.STATUS_NO_TRANSACTION;
    }
    
    public static boolean isStatusActive() throws SystemException
    {
        return getUserTransactionStatus() == Status.STATUS_NO_TRANSACTION;
    }
    
    public static void registerSynchronization(Synchronization synchronization) throws RollbackException, SystemException
    {
        DummyTransactionManager.getInstance().getTransaction().registerSynchronization(synchronization);
    }
}
