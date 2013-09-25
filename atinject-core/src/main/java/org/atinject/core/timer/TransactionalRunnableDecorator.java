package org.atinject.core.timer;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.atinject.core.cdi.CDI;

public class TransactionalRunnableDecorator implements Runnable {

    private Runnable delegate;
    
    public TransactionalRunnableDecorator(Runnable delegate){
        this.delegate = delegate;
    }
    
    @Override
    public void run() {
        UserTransaction userTransaction = CDI.select(UserTransaction.class).get();
        
        try {
            userTransaction.begin();
            try{
                delegate.run();
            }
            catch (Exception e) {
                userTransaction.setRollbackOnly();
                throw e;
            }
            finally {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                    userTransaction.commit();
                }
                else {
                    userTransaction.rollback();
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
