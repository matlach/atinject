package org.atinject.core.distexec;

import javax.enterprise.inject.Produces;

import org.atinject.core.cdi.Named;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;

public class DistributedExecutorMasterCacheNodeProducer {

    @Produces @Named("distributed-executor")
    public Configuration newDistributedExecutorMasterCacheNodeConfiguration() {
        return new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.DIST_SYNC).build();
    }
    
}
