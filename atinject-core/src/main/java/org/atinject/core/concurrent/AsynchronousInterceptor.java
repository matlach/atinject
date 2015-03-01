package org.atinject.core.concurrent;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Asynchronous
@Interceptor @Priority(Interceptor.Priority.APPLICATION)
public class AsynchronousInterceptor {

    @Inject
    private AsynchronousService asynchronousService;
    
    @AroundInvoke 
    public Object invokeAsynchronously(final InvocationContext ctx) throws Exception {
        Class<?> returnType = ctx.getMethod().getReturnType();
        if (void.class == returnType) {
        	asynchronousService.submit(() -> ctx.proceed());
        	return null;
        }
        return asynchronousService.submit(() -> ctx.proceed());
    }

}
