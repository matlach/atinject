package org.atinject.core.security;

import java.security.Principal;

import org.atinject.api.session.Session;

public class SessionPrincipal implements Principal {

    private Session session;

    public SessionPrincipal(){
        super();
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public SessionPrincipal setSession(Session session) {
        this.session = session;
        return this;
    }
    
    @Override
    public String getName() {
        if (session.getUserId() == null){
            return "anonymous";
        }
        return session.getUserId();
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SessionPrincipal other = (SessionPrincipal) obj;
        return other.getName().equals(other.getName());
    }
    
    
    
}
