package org.atinject.core.tiers.exception;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.core.websocket.dto.WebSocketResponse;
import org.atinject.core.websocket.dto.WebSocketResponseFault;

@ApplyExceptionMapper
@Interceptor @Priority(Interceptor.Priority.APPLICATION)
public class ApplyExceptionMapperInterceptor {
	
	@Inject
	private ExceptionMapperRegistryExtension exceptionMapperRegistryExtension;
	
    @AroundInvoke
    public Object applyExceptionMapper(InvocationContext invocationContext) throws Exception {
        try{
            return invocationContext.proceed();
        }
        catch (Exception e) {
            Class<?> rawReturnType = invocationContext.getMethod().getReturnType();
            if (! WebSocketResponse.class.isAssignableFrom(rawReturnType)) {
            	throw e;
            }
            Class<? extends WebSocketResponse> responseReturnType = (Class<? extends WebSocketResponse>) rawReturnType;
            WebSocketResponse response = responseReturnType.newInstance();
            WebSocketResponseFault responseFault = new WebSocketResponseFault();
            responseFault.setCause(e.getMessage());
            response.setFault(responseFault);
            
            return response;
        }
    }
}
