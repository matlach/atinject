package org.atinject.core.transaction;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class TransactionServices implements org.jboss.weld.transaction.spi.TransactionServices
{

    @Override
    public void cleanup()
    {
        // nothing to cleanup
    }

    @Override
    public void registerSynchronization(Synchronization synchronizedObserver)
    {
        try
        {
            TransactionManager.registerSynchronization(synchronizedObserver);
        }
        catch (RollbackException | SystemException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean isTransactionActive()
    {
        try
        {
            return TransactionManager.isStatusActive();
        }
        catch (SystemException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserTransaction getUserTransaction()
    {
        return TransactionManager.getUserTransaction();
    }

}
