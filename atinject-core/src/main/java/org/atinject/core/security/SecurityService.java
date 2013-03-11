package org.atinject.core.security;

import java.security.Principal;

import org.atinject.api.session.Session;

public class SecurityService implements org.jboss.weld.security.spi.SecurityServices {

    private static ThreadLocal<Session> currentSession;
    
    public static void setCurrentSession(Session session){
        currentSession.set(session);
    }
    
    public static void removeCurrentSession(){
        currentSession.remove();
    }
    
    @Override
    public void cleanup() {
        // nothing to cleanup
    }
    
    @Override
    public Principal getPrincipal() {
        return new SessionPrincipal().setSession(currentSession.get());
    }

}
