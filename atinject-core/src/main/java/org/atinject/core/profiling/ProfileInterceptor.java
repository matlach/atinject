package org.atinject.core.profiling;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Profile
@Interceptor
public class ProfileInterceptor {
    
    @Inject
    private ProfilingService profilingService;
    
    @AroundInvoke
    public Object profile(InvocationContext invocationContext) throws Exception{
        long t0 = System.nanoTime();
        try{
            return invocationContext.proceed();
        }
        // consider catching exception to gather exception statistics
        finally{
            profilingService.addProfiling(invocationContext, System.nanoTime() - t0);
        }
    }
}