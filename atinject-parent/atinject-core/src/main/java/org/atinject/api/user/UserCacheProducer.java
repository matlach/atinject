package org.atinject.api.user;

import javax.enterprise.inject.Produces;

import org.atinject.core.infinispan.CacheName;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;

public class UserCacheProducer
{
    @CacheName("user")
    @Produces
    public Configuration newCacheConfiguration() {
        return new ConfigurationBuilder()
                    .clustering()
                        .cacheMode(CacheMode.DIST_SYNC)
                    .loaders()
                        .addFileCacheStore()
//                            .async()
//                                .enable()
                            .purgeOnStartup(true)
                    .build();
    }
}
