package org.atinject.core.transaction;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Transactional
@Interceptor
public class TransactionalInterceptor
{

    // ?? @Inject UserTransaction userTransaction;
    
    @AroundInvoke 
    public Object manageTransaction(InvocationContext ctx) throws Exception
    {
        boolean firstInChain = false;
        if (TransactionManager.isStatusNoTransaction())
        {
            TransactionManager.begin();
            firstInChain = true;
        }
        
        try
        {
            return ctx.proceed();
        }
        catch (Exception e)
        {
            TransactionManager.setRollbackOnly();
            throw e;
        }
        finally
        {
            if (firstInChain)
            {
                if (TransactionManager.isStatusActive())
                {
                    TransactionManager.commit();
                }
                else
                {
                    TransactionManager.rollback();
                }
            }
        }
    }
}
