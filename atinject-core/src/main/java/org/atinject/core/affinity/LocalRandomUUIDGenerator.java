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

    @Inject @Named("distributed-executor") private DistributedCache<String, Object> cache;
    
    @Inject private AsynchronousService asynchronousService;
    
    private KeyAffinityService<String> keyAffinityService;
    
    @PostConstruct
    public void initialize(){
        keyAffinityService = KeyAffinityServiceFactory.newLocalKeyAffinityService(cache.unwrap(), new RendezvousIdKeyGenerator(), asynchronousService, 100);
    }
    
    public String getKey(){
        return keyAffinityService.getKeyForAddress(cache.getRpcManager().getAddress());
    }
    
    public static class RendezvousIdKeyGenerator implements KeyGenerator<String> {
        @Override
        public String getKey() {
            return UUID.randomUUID().toString();
        }
        
    }
    
}
