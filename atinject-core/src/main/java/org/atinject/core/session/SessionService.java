package org.atinject.core.session;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.session.event.SessionClosed;
import org.atinject.core.session.event.SessionOpened;
import org.atinject.core.tiers.Service;
import org.atinject.core.topology.TopologyService;

@Service
public class SessionService {
    
    @Inject @Named("session") private ReplicatedCache<String, Session> sessionCache;
    
    public Optional<? extends Session> getSession(String sessionId){
        return sessionCache.get(sessionId);
    }

    public Map<String, Optional<Session>> getAllSessions(String... sessionIds) {
        return sessionCache.getAll(sessionIds);
    }
    
    public Map<String, Optional<Session>> getAllSessions(List<String> sessionIds) {
        return sessionCache.getAll(sessionIds);
    }
    
    public List<Session> getAllSessions(Predicate<Session> predicate) {
    	return sessionCache.values()
    			.filter(predicate)
    			.collect(Collectors.toList());
    }
    
    public List<Session> getAllSessionsByMachineId(String machineId) {
        return getAllSessions(SessionSpecifications.matchingMachineId(machineId));
    }
    
    public List<Session> getAllSessionByMachineIds(Collection<String> machineIds) {
        return getAllSessions(SessionSpecifications.matchingMachineId(machineIds));
    }
    
    public void put(Session session) {
        sessionCache.put(session.getSessionId(), session);
    }
    
    public void remove(Session session) {
        sessionCache.remove(session.getSessionId());
    }
    
    public void removeAllSession(Collection<Session> sessions) {
        for (Session session : sessions) {
            sessionCache.remove(session.getSessionId());
        }
    }
    
    @Inject private Event<SessionOpened> sessionOpenedEvent;
    
    @Inject private Event<SessionClosed> sessionClosedEvent;
    
    @Inject private TopologyService topologyService;
    
    @PreDestroy
    public void cleanUp() {
        String machineId = topologyService.getLocalAddress().getMachineId();
        List<Session> sessions = getAllSessionsByMachineId(machineId);
        for (Session session : sessions) {
            sessionCache.remove(session.getSessionId());
        }
    }
    
    // what happen if session open / close get mixed up, potential leak ? can we ensure netty will fire event in the right order ?
    public void openSession(Session session) {
        sessionCache.put(session.getSessionId(), session); // this will replicate
        SessionOpened sessionOpened = new SessionOpened().setSession(session);
        sessionOpenedEvent.fire(sessionOpened);
    }
    
    public void closeSession(Session session) {
        sessionCache.remove(session.getSessionId()); // this will replicate
        SessionClosed sessionClosed = new SessionClosed().setSession(session);
        sessionClosedEvent.fire(sessionClosed);
    }
    
    public List<Session> getAllSessionsByRackId(String rackId) {
        return getAllSessionByMachineIds(topologyService.getAllMachineIdBySiteId(rackId));
    }
    
    public List<Session> getAllSessionsBySiteId(String siteId) {
        return getAllSessionByMachineIds(topologyService.getAllMachineIdBySiteId(siteId));
    }
    
    public void updateSession(Session session) {
        sessionCache.put(session.getSessionId(), session);
    }
}
