package org.atinject.api.session;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.InfinispanCache;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;

/**
 * Note : session cache should not be persisted by any mean
 */
@ApplicationScoped
public class SessionCache
{

    @Inject @CacheName("session")
    private InfinispanCache<String, Session> cache;
    
    public Session getSession(String sessionId){
        return cache.get(sessionId);
    }

    /**
     * Note : this method use map reduce operation to search
     */
    public Session getSessionByUserId(String userId){
        return cache.performMapReduce(
                new GetSessionByUserIdMapper(userId),
                new GetSessionByUserIdReducer(),
                new GetSessionByUserIdCollator());
    }
    
    public static class GetSessionByUserIdMapper implements Mapper<String, Session, String, Session>{
        private static final long serialVersionUID = 1L;

        private String userId;
        
        public GetSessionByUserIdMapper(String userId){
            this.userId = userId;
        }
        
        @Override
        public void map(String key, Session value, Collector<String, Session> collector) {
            if (value.getUserId().equals(userId)){
                collector.emit(value.getUserId(), value);
            }
        }
    }
    
    public static class GetSessionByUserIdReducer implements Reducer<String, Session>{
        private static final long serialVersionUID = 1L;

        @Override
        public Session reduce(String reducedKey, Iterator<Session> iter) {
            if (! iter.hasNext()){
                return null;
            }
            return iter.next();
        }
    }

    public static class GetSessionByUserIdCollator implements Collator<String, Session, Session>{
        @Override
        public Session collate(Map<String, Session> reducedResults)
        {
            Iterator<Session> iterator = reducedResults.values().iterator();
            if (iterator.hasNext()){
                return iterator.next();
            }
            return null;
        }
    }
    
    public Map<String, Session> getAllSessions(String... sessionIds){
        return cache.getAll(sessionIds);
    }
    
    public Map<String, Session> getAllSessions(List<String> sessionIds){
        return cache.getAll(sessionIds);
    }
    
    public List<Session> getAllSessionsByMachineId(){
        return null;
    }
    
    public List<Session> getAllSessionsByRackId(){
        return null;
    }
    
    public List<Session> getAllSessionsBySiteId(){
        return null;
    }
    
    public void lock(String sessionId){
        cache.lock(sessionId);
    }
    
    public void lock(String... sessionIds){
        cache.lock(sessionIds);
    }
    
    public void lock(List<String> sessionIds){
        cache.lock(sessionIds);
    }
    
    public void put(Session session){
        cache.put(session.getSessionId(), session);
    }
    
    public void remove(Session session){
        cache.remove(session.getSessionId());
    }
}
