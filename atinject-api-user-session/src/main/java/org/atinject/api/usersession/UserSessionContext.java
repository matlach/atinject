package org.atinject.api.usersession;

import org.atinject.core.session.SessionContext;

public final class UserSessionContext {

    private UserSessionContext() {
        
    }
    
    public static UserSession getCurrentSession(){
        return (UserSession) SessionContext.getCurrentSession();
    }
    
    public static void setCurrentSession(UserSession session){
        SessionContext.setCurrentSession(session);
    }
    
    public static void removeCurrentSession(){
        SessionContext.removeCurrentSession();
    }
}
