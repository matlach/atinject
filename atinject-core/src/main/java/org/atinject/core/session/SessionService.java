package org.atinject.core.session;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.core.session.event.SessionClosed;
import org.atinject.core.session.event.SessionOpened;
import org.atinject.core.topology.TopologyService;

@ApplicationScoped
public class SessionService{
    
    @Inject SessionCache sessionCache;
    
    @Inject Event<SessionOpened> sessionOpenedEvent;
    
    @Inject Event<SessionClosed> sessionClosedEvent;
    
    @Inject TopologyService topologyService;
    
    @PreDestroy
    public void cleanUp() {
        String machineId = topologyService.getLocalAddress().getMachineId();
        List<Session> sessions = getAllSessionsByMachineId(machineId);
        sessionCache.removeAllSession(sessions);
    }
    
    // what happen if session open / close get mixed up, potential leak ? can we ensure netty will fire event in the right order ?
    public void openSession(Session session) {
        sessionCache.put(session); // this will replicate
        SessionOpened sessionOpened = new SessionOpened().setSession(session);
        sessionOpenedEvent.fire(sessionOpened);
    }
    
    public void closeSession(Session session) {
        sessionCache.remove(session); // this will replicate
        SessionClosed sessionClosed = new SessionClosed().setSession(session);
        sessionClosedEvent.fire(sessionClosed);
    }
    
    public Session getSession(String sessionId) {
        return sessionCache.getSession(sessionId);
    }

    public List<Session> getAllSessionsByMachineId(String machineId) {
        return sessionCache.getAllSessionsByMachineId(machineId);
    }
    
    public List<Session> getAllSessionsByRackId(String rackId) {
        return sessionCache.getAllSessionByMachineIds(topologyService.getAllMachineIdBySiteId(rackId));
    }
    
    public List<Session> getAllSessionsBySiteId(String siteId) {
        return sessionCache.getAllSessionByMachineIds(topologyService.getAllMachineIdBySiteId(siteId));
    }
    
    public void updateSession(Session session) {
        sessionCache.put(session);
    }
}
