package org.atinject.api.rendezvous;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.cache.ClusteredCacheManager;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

public class RendezvousCacheProducer {

    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @CacheName("rendez-vous")
    public Configuration newCacheConfiguration() {
        return new ConfigurationBuilder()
                    .clustering()
                        .cacheMode(CacheMode.DIST_SYNC)
                    .hash()
                        .numOwners(1)
                    .loaders()
                        .addFileCacheStore()
                            .async()
                                .enable()
                            .purgeOnStartup(true)
                    .transaction()
                        .transactionMode(TransactionMode.TRANSACTIONAL)
                        .lockingMode(LockingMode.PESSIMISTIC)
                        .useSynchronization(true)
                        .transactionManagerLookup(new DummyTransactionManagerLookup())
                    .build();
    }
    
    @Produces @CacheName("rendez-vous")
    public ClusteredCache<String, RendezvousEntity> newClusteredCache(){
        Cache<String, RendezvousEntity> cache = cacheManager.getCache("rendez-vous");
        return new ClusteredCache<>(cache);
    }
    
    @Produces @CacheName("rendez-vous") Cache<String, RendezvousEntity> newCache(){
        return cacheManager.getCache("rendez-vous");
    }
    
}
