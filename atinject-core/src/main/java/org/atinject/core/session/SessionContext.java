package org.atinject.core.session;

public final class SessionContext {

    // XXX find a way to make sure that any thread, get cleaned up after run
    private static ThreadLocal<Session> currentSession = new InheritableThreadLocal<>();
    
    private SessionContext() {
        
    }
    
    public static Session getCurrentSession(){
        return currentSession.get();
    }
    
    public static void setCurrentSession(Session session){
        currentSession.set(session);
    }
    
    public static void removeCurrentSession(){
        currentSession.remove();
    }
    
}
