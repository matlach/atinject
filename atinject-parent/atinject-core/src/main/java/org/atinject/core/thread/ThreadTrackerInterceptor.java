package org.atinject.core.thread;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ThreadTracker
@Interceptor
public class ThreadTrackerInterceptor {
    @AroundInvoke
    public Object annotateThread(InvocationContext invocationContext) throws Exception{
        String originName = Thread.currentThread().getName();
        try{
            String tracingName = invocationContext.getMethod().getName() + " " + originName;
            Thread.currentThread().setName(tracingName);
            return invocationContext.proceed();
        }finally{
            Thread.currentThread().setName(originName);
        }
    }
}
