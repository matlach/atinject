package org.atinject.core.transaction;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@InfinispanTransactional
@Interceptor
public class InfinispanTransactionalInterceptor
{

    @AroundInvoke 
    public Object manageTransaction(InvocationContext ctx) throws Exception
    {
        boolean firstInChain = false;
        if (InfinispanTransactionManager.isStatusNoTransaction())
        {
            InfinispanTransactionManager.begin();
            firstInChain = true;
        }
        
        try
        {
            return ctx.proceed();
        }
        catch (Exception e)
        {
            InfinispanTransactionManager.setRollbackOnly();
            throw e;
        }
        finally
        {
            if (firstInChain)
            {
                if (InfinispanTransactionManager.isStatusActive())
                {
                    InfinispanTransactionManager.commit();
                }
                else
                {
                    InfinispanTransactionManager.rollback();
                }
            }
        }
    }
}
