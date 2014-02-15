package org.atinject.core.session;

import org.atinject.core.concurrent.InheritableContext;

public final class SessionContext {

    private static final String SESSION_KEY = "session";
    
    private SessionContext() {
        
    }
    
    public static Session get(){
        return (Session) InheritableContext.get(SESSION_KEY);
    }
    
    public static void set(Session session){
        InheritableContext.set(SESSION_KEY, session);
    }
    
    public static void remove() {
        InheritableContext.remove(SESSION_KEY);
    }
    
}
