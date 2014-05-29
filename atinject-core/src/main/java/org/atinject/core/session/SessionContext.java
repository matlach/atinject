package org.atinject.core.session;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.concurrent.InheritableContext;

@ApplicationScoped
public class SessionContext {

    private static final String SESSION_KEY = "session";
    
    public Session get(){
        return (Session) InheritableContext.get(SESSION_KEY);
    }
    
    public void set(Session session){
        InheritableContext.set(SESSION_KEY, session);
    }
    
    public void remove() {
        InheritableContext.remove(SESSION_KEY);
    }
    
}
