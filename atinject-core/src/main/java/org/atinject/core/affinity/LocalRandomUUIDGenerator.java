package org.atinject.core.affinity;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.concurrent.AsynchronousService;
import org.infinispan.affinity.KeyAffinityService;
import org.infinispan.affinity.KeyAffinityServiceFactory;
import org.infinispan.affinity.KeyGenerator;

@ApplicationScoped
public class LocalRandomUUIDGenerator {

    @Inject @Named("distributed-executor") private DistributedCache<UUID, Object> cache;
    
    @Inject private AsynchronousService asynchronousService;
    
    private KeyAffinityService<UUID> keyAffinityService;
    
    @PostConstruct
    public void initialize(){
        keyAffinityService = KeyAffinityServiceFactory.newLocalKeyAffinityService(cache.unwrap(), new LocalRandomUUIDKeyGenerator(), asynchronousService, 100);
    }
    
    public UUID getKey(){
        return keyAffinityService.getKeyForAddress(cache.getRpcManager().getAddress());
    }
    
    public static class LocalRandomUUIDKeyGenerator implements KeyGenerator<UUID> {
        @Override
        public UUID getKey() {
            return UUID.randomUUID();
        }
        
    }
    
}
