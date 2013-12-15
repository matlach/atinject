package org.atinject.core.tiers.exception;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@HandleCacheException
@Interceptor
public class HandleCacheExceptionInterceptor {
    
    @Inject private Logger logger;
    @Inject private CacheExceptionSanitizer cacheStoreExceptionSanitizer;
    
    private boolean sanitize = true;
    private boolean log = true;
    
    @AroundInvoke
    public Object handleException(InvocationContext invocationContext) throws Exception{
        try{
            return invocationContext.proceed();
        }
        catch (Exception e){
            if (sanitize) {
                cacheStoreExceptionSanitizer.sanitize(e);
            }
            if (log) {
                logger.error("", e);
            }
            throw e;
        }
    }
}