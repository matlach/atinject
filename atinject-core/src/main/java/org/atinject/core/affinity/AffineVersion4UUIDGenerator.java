package org.atinject.core.affinity;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.topology.TopologyService;
import org.atinject.core.uuid.Version4UUIDGenerator;
import org.infinispan.affinity.KeyAffinityService;
import org.infinispan.affinity.KeyAffinityServiceFactory;
import org.infinispan.affinity.KeyGenerator;

@ApplicationScoped
public class AffineVersion4UUIDGenerator {

    @Inject @Named("distributed-executor")
    private DistributedCache<UUID, Object> cache;
    
    @Inject
    private AsynchronousService asynchronousService;
    
    @Inject
    private Version4UUIDGenerator version4UUIDGenerator;
    
    private KeyAffinityService<UUID> keyAffinityService;
    
    private static final int keyBufferSize = 100;
    
    @Inject
    private TopologyService topologyService;
    
    @PostConstruct
    public void initialize() {
        KeyAffinityServiceFactory.newKeyAffinityService(
                cache.unwrap(), asynchronousService, new Version4UUIDKeyGenerator(), keyBufferSize);
    }
    
    public UUID getLocalKey() {
        return keyAffinityService.getKeyForAddress(topologyService.getLocalAddress());
    }
    
    public UUID getRemoteKey(String machineId) {
        return keyAffinityService.getKeyForAddress(topologyService.getAddress(machineId));
    }
    
    public class Version4UUIDKeyGenerator implements KeyGenerator<UUID> {
        @Override
        public UUID getKey() {
            return version4UUIDGenerator.get();
        }
        
    }
    
}
