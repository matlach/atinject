package org.atinject.core.transaction;

import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.infinispan.transaction.tm.DummyTransactionManager;
import org.jboss.weld.transaction.spi.TransactionServices;

public class InMemoryTransactionServices implements TransactionServices
{

    @Override
    public void cleanup() {
        // nothing to cleanup
    }

    @Override
    public void registerSynchronization(Synchronization synchronizedObserver)
    {
        try {
            DummyTransactionManager.getInstance().getTransaction().registerSynchronization(synchronizedObserver);
        }
        catch (RollbackException | SystemException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean isTransactionActive()
    {
        try {
            return getUserTransaction().getStatus() == Status.STATUS_ACTIVE;
        }
        catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserTransaction getUserTransaction() {
        return DummyTransactionManager.getUserTransaction();
    }

}
