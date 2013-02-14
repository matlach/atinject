package org.atinject.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Retry
@Interceptor
public class RetryInterceptor
{

    private static ThreadLocal<Object> hack = new ThreadLocal<Object>();
    
    private int RETRY_COUNT = 3;
    private int MIN_TIMEOUT = 100;
    
    @Inject
    private ScheduledService scheduledService;
    
    @AroundInvoke 
    public Object invokeWithRetry(final InvocationContext ctx) throws Exception
    {
        // we assume RetryInterceptor is the first in chain
        if (hack.get() == null)
        {
            long delay = 0;
            for (int i = 0 ; i < RETRY_COUNT; i++)
            {
                Future<Object> future = scheduledService.schedule(new Callable<Object>(){
                    @Override
                    public Object call() throws Exception
                    {
                        hack.set(new Object());
                        try
                        {
                            return ctx.getMethod().invoke(ctx.getTarget(), ctx.getParameters());
                        }
                        finally
                        {
                            hack.remove();
                        }
                    }}, delay, TimeUnit.MILLISECONDS);
                
                try
                {
                    return future.get();
                }
                catch (ExecutionException e)
                {
                    if (i == (RETRY_COUNT - 1))
                    {
                        throw new Exception(e.getCause());
                    }
                    delay = MIN_TIMEOUT + (long) (MIN_TIMEOUT * Math.random() * (i + 1));
                }
            }
        }
        else
        {
            return ctx.proceed();
        }
        throw new AssertionError("should have return value or thrown exception");
    }

}
