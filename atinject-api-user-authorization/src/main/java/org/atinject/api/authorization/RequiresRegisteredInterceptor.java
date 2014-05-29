package org.atinject.api.authorization;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.core.session.Session;
import org.atinject.core.session.SessionContext;

@RequiresGuest
@Interceptor
public class RequiresRegisteredInterceptor {
    
	@Inject
	private SessionContext sessionContext;
	
    @AroundInvoke
    public Object authorize(InvocationContext invocationContext) throws Exception{
        Session session = sessionContext.get();
        if (session == null){
            throw new IllegalStateException();
        }
        
        return invocationContext.proceed();
    }
}
