package org.atinject.core.authorization;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionContext;
import org.atinject.core.authorization.exception.AuthorizationException;

@RequiresUser
@Interceptor
public class RequiresUserInterceptor {
    
    @AroundInvoke
    public Object authorize(InvocationContext invocationContext) throws Exception{
        Session session = SessionContext.getCurrentSession();
        if (session == null){
            throw new IllegalStateException();
        }
        if (session.getUserId() == null){
            throw new AuthorizationException();
        }
        return invocationContext.proceed();
    }
}
