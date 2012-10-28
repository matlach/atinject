package org.atinject.core.distexec;

import javax.enterprise.inject.Produces;

import org.atinject.core.infinispan.CacheName;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;

public class DistributedExecutorMasterCacheNodeConfigurationProducer {

	@CacheName("distributed-executor-master-cache-node")
    @Produces
    public Configuration newDistributedExecutorMasterCacheNodeConfiguration() {
        return new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.DIST_SYNC).build();
    }
}
