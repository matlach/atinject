package org.atinject.core.exception;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@HandleServiceException
@Interceptor
public class HandleWebSocketServiceExceptionInterceptor {
    
    @Inject private Logger logger;
    @Inject private WebSocketServiceExceptionSanitizer webSocketServiceExceptionSanitizer;
    
    @AroundInvoke
    public Object handleException(InvocationContext invocationContext) throws Exception{
        try{
            return invocationContext.proceed();
        }
        catch (Exception e){
            webSocketServiceExceptionSanitizer.sanitize(e);
            logger.error("", e);
            throw e;
        }
    }
}