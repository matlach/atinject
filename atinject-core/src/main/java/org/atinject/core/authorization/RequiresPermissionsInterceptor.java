package org.atinject.core.authorization;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionContext;

@RequiresPermissions
@Interceptor
public class RequiresPermissionsInterceptor {
    
    @AroundInvoke
    public Object authorize(InvocationContext invocationContext) throws Exception{
        Session session = SessionContext.getCurrentSession();
        if (session == null){
            throw new IllegalStateException();
        }
        
        return invocationContext.proceed();
    }
}
