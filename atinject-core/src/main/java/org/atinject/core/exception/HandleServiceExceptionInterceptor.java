package org.atinject.core.exception;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@HandleServiceException
@Interceptor
public class HandleServiceExceptionInterceptor {
    
    @Inject private Logger logger;
    @Inject private ServiceExceptionSanitizer serviceExceptionSanitizer;
    
    @AroundInvoke
    public Object handleException(InvocationContext invocationContext) throws Exception{
        try{
            return invocationContext.proceed();
        }
        catch (Exception e){
            serviceExceptionSanitizer.sanitize(e);
            logger.error("", e);
            throw e;
        }
    }
}