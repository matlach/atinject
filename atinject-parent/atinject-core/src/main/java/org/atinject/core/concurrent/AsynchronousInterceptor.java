package org.atinject.core.concurrent;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Asynchronous
@Interceptor
public class AsynchronousInterceptor
{

    private static ThreadLocal<Object> hack = new ThreadLocal<Object>();
    
    @Inject
    private AsynchronousService asynchronousService;
    
    @AroundInvoke 
    public Object runAsync(final InvocationContext ctx) throws Exception
    {
        // we assume AsynchronousInterceptor is the first in chain
        if (hack.get() == null)
        {
            asynchronousService.submit(new Callable<Void>(){
                @Override
                public Void call() throws Exception
                {
                    hack.set(new Object());
                    try
                    {
                        ctx.getMethod().invoke(ctx.getTarget(), ctx.getParameters());
                    }
                    finally
                    {
                        hack.remove();
                    }
                    return null;
                }});
        }
        else
        {
            ctx.proceed();
        }
        return null;
    }

}
