package org.atinject.api.session;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.session.event.SessionClosed;
import org.atinject.api.session.event.SessionOpened;

@ApplicationScoped
public class SessionService{
    
    @Inject
    private SessionCache sessionCache;
    
    // what happen if session open / close get mixed up, potential leak ? can we ensure netty will fire event in the right order ?
    public void onSessionOpened(@Observes SessionOpened event){
        sessionCache.put(event.getSession()); // this will replicate
    }
    
    public void onSessionClosed(@Observes SessionClosed event){
        sessionCache.remove(event.getSession()); // this will replicate
        // fire an event that user will probably listen, then user will fire an event as well
    }
    
    public Session getSession(String sessionId){
        return sessionCache.getSession(sessionId);
    }

    public Session getSessionByUserId(String userId){
        return sessionCache.getSessionByUserId(userId);
    }
    
    public List<Session> getAllSessionsByMachineId(){
        return sessionCache.getAllSessionsByMachineId();
    }
    
    public List<Session> getAllSessionsByRackId(){
        return sessionCache.getAllSessionsByRackId();
    }
    
    public List<Session> getAllSessionsBySiteId(){
        return sessionCache.getAllSessionsBySiteId();
    }
    
    public void updateSession(Session session){
        sessionCache.put(session);
    }
}
