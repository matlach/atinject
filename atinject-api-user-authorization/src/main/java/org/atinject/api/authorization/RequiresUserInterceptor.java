package org.atinject.api.authorization;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.api.authorization.exception.AuthorizationException;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.session.SessionContext;

@RequiresUser
@Interceptor
public class RequiresUserInterceptor {
    
    @AroundInvoke
    public Object authorize(InvocationContext invocationContext) throws Exception{
        UserSession session = (UserSession) SessionContext.get();
        if (session == null){
            throw new IllegalStateException();
        }
        if (session.getUserId() == null){
            throw new AuthorizationException();
        }
        return invocationContext.proceed();
    }
}
