package org.atinject.core.distexec;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.core.cache.ClusteredCacheManager;
import org.atinject.core.cdi.Named;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;

public class DistributedExecutorMasterCacheNodeProducer {

    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @Named("distributed-executor")
    public Configuration newDistributedExecutorMasterCacheNodeConfiguration() {
        return new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.DIST_SYNC).build();
    }
    
    @Produces @Named("distributed-executor")
    public Cache<Object, Object> newCache(){
        return cacheManager.getCache("distributed-executor");
    }
}
