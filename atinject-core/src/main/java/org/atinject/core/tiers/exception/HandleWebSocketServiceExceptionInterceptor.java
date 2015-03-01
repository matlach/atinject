package org.atinject.core.tiers.exception;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@HandleWebSocketServiceException
@Interceptor @Priority(Interceptor.Priority.APPLICATION)
public class HandleWebSocketServiceExceptionInterceptor {
    
    @Inject private Logger logger;
    @Inject private WebSocketServiceExceptionSanitizer webSocketServiceExceptionSanitizer;
    
    private boolean sanitize = true;
    private boolean log = true;
    
    @AroundInvoke
    public Object handleException(InvocationContext invocationContext) throws Exception{
        try{
            return invocationContext.proceed();
        }
        catch (Exception e){
            if (sanitize) {
                webSocketServiceExceptionSanitizer.sanitize(e);
            }
            if (log) {
                logger.error("", e);
            }
            throw e;
        }
    }
}