package org.atinject.core.transaction;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

@Transactional
@Interceptor
public class TransactionalInterceptor
{

    @Inject private UserTransaction userTransaction;
    
    @AroundInvoke 
    public Object manageTransaction(InvocationContext ctx) throws Exception {
        boolean firstInChain = false;
        if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
            userTransaction.begin();
            firstInChain = true;
        }
        
        try {
            return ctx.proceed();
        }
        catch (Exception e) {
            userTransaction.setRollbackOnly();
            throw e;
        }
        finally {
            if (firstInChain) {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                    userTransaction.commit();
                }
                else {
                    userTransaction.rollback();
                }
            }
        }
    }
}
