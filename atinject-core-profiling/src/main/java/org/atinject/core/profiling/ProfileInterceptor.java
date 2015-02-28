package org.atinject.core.profiling;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.codahale.metrics.Timer.Context;

@Profile
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class ProfileInterceptor {
    
    @Inject
    private ProfilingService profilingService;
    
    @AroundInvoke
    public Object profile(InvocationContext invocationContext) throws Exception {
    	profilingService.beforeInvocation(invocationContext);
    	Exception exception = null;
        try (Context timerContext = profilingService.getTimerContext(invocationContext)) {
            return invocationContext.proceed();
        }
        catch (Exception e) {
        	exception = e;
        	throw e;
        }
        finally {
        	profilingService.afterInvocation(invocationContext, exception);
        }
    }
}