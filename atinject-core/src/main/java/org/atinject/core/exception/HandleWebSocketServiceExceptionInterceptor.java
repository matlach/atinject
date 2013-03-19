package org.atinject.core.exception;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@HandleWebSocketServiceException
@Interceptor
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
            throw e; // TODO do not throw, need to create WebSocketResponse and WebSocketResponse.exception dynamically
        }
    }
}