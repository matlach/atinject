package org.atinject.api.rendezvous;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.concurrent.AsynchronousService;
import org.infinispan.Cache;
import org.infinispan.affinity.KeyAffinityService;
import org.infinispan.affinity.KeyAffinityServiceFactory;
import org.infinispan.affinity.KeyGenerator;

@ApplicationScoped
public class RendezvousIdGenerator {

    @Inject @CacheName("rendezvous") private Cache<String, RendezvousEntity> cache;
    
    @Inject private AsynchronousService asynchronousService;
    
    private KeyAffinityService<String> keyAffinityService;
    
    @PostConstruct
    public void initialize(){
        keyAffinityService = KeyAffinityServiceFactory.newLocalKeyAffinityService(cache, new RendezvousIdKeyGenerator(), asynchronousService, 100);
    }
    
    public String getKey(){
        return keyAffinityService.getKeyForAddress(cache.getAdvancedCache().getRpcManager().getAddress());
    }
    
    public static class RendezvousIdKeyGenerator implements KeyGenerator<String> {
        @Override
        public String getKey() {
            return UUID.randomUUID().toString();
        }
        
    }
    
}
