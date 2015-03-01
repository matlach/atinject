package org.atinject.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Retry
@Interceptor @Priority(Interceptor.Priority.APPLICATION)
public class RetryInterceptor {

    private static ThreadLocal<Object> hack = new ThreadLocal<>();
    
    @Inject
    private ScheduledService scheduledService;
    
    @AroundInvoke 
    public Object invokeWithRetry(final InvocationContext ctx) throws Exception {
        // we assume RetryInterceptor is the first in chain
        if (hack.get() != null) {
            return ctx.proceed();
        }
        
        Retry retryAnnotation = getRetryAnnotation(ctx);
        long delay = 0;
        for (int i = 0 ; i < retryAnnotation.count(); i++) {
            Future<Object> future = scheduledService.schedule(new Callable<Object>(){
                @Override
                public Object call() throws Exception {
                    hack.set(new Object());
                    try {
                        return ctx.getMethod().invoke(ctx.getTarget(), ctx.getParameters());
                    }
                    finally {
                        hack.remove();
                    }
                }}, delay, TimeUnit.MILLISECONDS);
            
            try {
                return future.get();
            }
            catch (ExecutionException e) {
                if (i == (retryAnnotation.count() - 1)) {
                    throw new Exception(e.getCause());
                }
                delay = retryAnnotation.timeout() + (long) (retryAnnotation.count() * Math.random() * (i + 1));
            }
        }
        
        throw new AssertionError("should have return value or thrown exception");
    }
    
    private Retry getRetryAnnotation(InvocationContext ctx) {
        Retry retry = ctx.getMethod().getAnnotation(Retry.class);
        if (retry != null){
            return retry;
        }
        return ctx.getTarget().getClass().getAnnotation(Retry.class);
    }

}
