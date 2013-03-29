package org.atinject.api.rendezvous;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class RendezvousCache extends CacheStore {

    @Inject private ClusteredCache<String, RendezvousEntity> cache;
    
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
}
