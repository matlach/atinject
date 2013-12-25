package org.atinject.core.session;

public final class SessionContext {

    private static ThreadLocal<Session> currentSession;
    
    public static Session getCurrentSession(){
        return currentSession.get();
    }
    
    public static void setCurrentSession(Session session){
        currentSession.set(session);
    }
    
    public static void removeCurrentSession(){
        currentSession.remove();
    }
    
    private SessionContext() {
        
    }
}
