package org.atinject.core.transaction;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.weld.transaction.spi.TransactionServices;

public class InfinispanTransactionServices implements TransactionServices
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
            InfinispanTransactionManager.registerSynchronization(synchronizedObserver);
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
            return InfinispanTransactionManager.isStatusActive();
        }
        catch (SystemException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserTransaction getUserTransaction()
    {
        return InfinispanTransactionManager.getUserTransaction();
    }

}
