package org.atinject.api.session;

public class SessionContext {

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
}
