package org.atinject.api.session;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class SessionService{
    
    @Inject
    private SessionCacheStore sessionCacheStore;
    
    public void onSessionOpened(@Observes SessionOpened event){
//        if (event.isOriginLocal()){
//            sessionCacheStore.put(event.getSession()); // this will replicate
//        }
//        send notification to current session, session has been registered
    }
    
    public void onSessionClosed(@Observes SessionClosed event){
//        if (event.isOriginLocal()){
//            sessionCacheStore.remove(event.getSession()); // this will replicate
//        }
    }
    
    public void updateSession(Session session){
//        sessionCacheStore.lock(session.getSessionId());
//        sessionCacheStore.put(session);
    }
}
