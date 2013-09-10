package org.atinject.api.rendezvous;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class RendezvousCache extends CacheStore {

    @Inject @CacheName("rendezvous") private ClusteredCache<String, RendezvousEntity> cache;
    
    @Inject private RendezvousIdGenerator generator;
    
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
