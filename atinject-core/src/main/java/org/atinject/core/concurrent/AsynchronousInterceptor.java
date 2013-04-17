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
    public Object invokeAsynchronously(final InvocationContext ctx) throws Exception {
        // we assume AsynchronousInterceptor is the first in chain
        if (hack.get() != null){
            return ctx.proceed();
        }
        
        return asynchronousService.submit(new Callable<Object>(){
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
            }
        });
    }

}
