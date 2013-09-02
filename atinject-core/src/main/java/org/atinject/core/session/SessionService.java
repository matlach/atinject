package org.atinject.core.session;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.core.session.event.SessionClosed;
import org.atinject.core.session.event.SessionOpened;
import org.atinject.core.topology.TopologyService;

@ApplicationScoped
public class SessionService{
    
    @Inject protected SessionCache sessionCache;
    
    @Inject protected TopologyService topologyService;
    
    @PreDestroy
    public void cleanUp() {
        String machineId = topologyService.getLocalAddress().getMachineId();
        // TODO sessionCache.removeAllSessionByMachineId(machineId);
    }
    
    // what happen if session open / close get mixed up, potential leak ? can we ensure netty will fire event in the right order ?
    public void onSessionOpened(@Observes SessionOpened event) {
        sessionCache.put(event.getSession()); // this will replicate
    }
    
    public void onSessionClosed(@Observes SessionClosed event) {
        sessionCache.remove(event.getSession()); // this will replicate
        // fire an event that user will probably listen, then user will fire an event as well
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
