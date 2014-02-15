package org.atinject.api.usersession;

import org.atinject.core.session.SessionContext;

public final class UserSessionContext {

    private UserSessionContext() {
        
    }
    
    public static UserSession get() {
        return (UserSession) SessionContext.get();
    }
    
    public static void set(UserSession session) {
        SessionContext.set(session);
    }
    
    public static void remove() {
        SessionContext.remove();
    }
}
