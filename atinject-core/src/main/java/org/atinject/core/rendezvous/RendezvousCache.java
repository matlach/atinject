package org.atinject.core.rendezvous;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.affinity.LocalRandomUUIDGenerator;
import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.rendezvous.entity.RendezvousEntity;
import org.atinject.core.tiers.CacheStore;

/**
 * read access :
 * - each time a session want to interact with all sessions within the rendezvous (get)
 * - each time a session close (get all)
 * write access :
 * - each time a session create a rendezvous (put)
 * - each time a session join a rendezvous (put)
 * - each time a session leave a rendezvous (put)
 * - one last time when the last session leave a rendezvous (remove)
 */
@ApplicationScoped
public class RendezvousCache extends CacheStore {

    @Inject @Named("rendezvous") private ReplicatedCache<String, RendezvousEntity> cache;
    
    @Inject private LocalRandomUUIDGenerator generator;
    
    public String getId(){
        return generator.getKey();
    }
    
    public RendezvousEntity getRendezvous(String rendezvousId){
        return cache.get(rendezvousId);
    }
    
    public void lockRendezvous(String rendezvousId){
        cache.lock(rendezvousId);
    }
    
    public void putRendezvous(RendezvousEntity rendezvous){
        cache.put(rendezvous.getId(), rendezvous);
    }
    
    public void removeRendezvous(RendezvousEntity rendezvous){
        cache.remove(rendezvous.getId());
    }
    
    /**
     * note : it is assumed that cache is distributed
     * and thus will only returns local rendezvous
     */
    public List<RendezvousEntity> getAllRendezvous() {
        return new ArrayList<>(cache.values());
    }
}
