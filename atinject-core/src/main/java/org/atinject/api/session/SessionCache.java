package org.atinject.api.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

/**
 * Note : session cache be replicated, optimistic and should not be persisted by any mean
 */
@ApplicationScoped
public class SessionCache extends CacheStore {
    
    @Inject @CacheName("session") private ClusteredCache<String, Session> cache;
    
    public Session getSession(String sessionId){
        return cache.get(sessionId);
    }

    public Session getSessionByUserId(String userId){
        for (Session session : cache.values()){
            if (session.getUserId().equals(userId)){
                return session;
            }
        }
        return null;
    }
    
    public Map<String, Session> getAllSessions(String... sessionIds){
        return cache.getAll(sessionIds);
    }
    
    public Map<String, Session> getAllSessions(List<String> sessionIds){
        return cache.getAll(sessionIds);
    }
    
    public List<Session> getAllSessionsByMachineId(String machineId){
        List<Session> sessions = new ArrayList<>();
        for (Session session : cache.values()){
            if (session.getMachineId().equals(machineId)){
                sessions.add(session);
            }
        }
        return sessions;
    }
    
    public List<Session> getAllSessionByMachineIds(Collection<String> machineIds){
        List<Session> sessions = new ArrayList<>();
        for (Session session : cache.values()){
            if (machineIds.contains(session.getMachineId())){
                sessions.add(session);
            }
        }
        return sessions;
    }
    
    public void put(Session session){
        cache.put(session.getSessionId(), session);
    }
    
    public void remove(Session session){
        cache.remove(session.getSessionId());
    }
}
