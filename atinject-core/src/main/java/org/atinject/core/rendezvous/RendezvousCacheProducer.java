package org.atinject.core.rendezvous;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.cache.ClusteredCacheManager;
import org.atinject.core.rendezvous.entity.RendezvousEntity;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

public class RendezvousCacheProducer {

    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @CacheName("rendezvous")
    public Configuration newCacheConfiguration() {
        return new ConfigurationBuilder()
                    .clustering()
                        .cacheMode(CacheMode.DIST_SYNC)
                    .hash()
                        .numOwners(1)
                    .transaction()
                        .transactionMode(TransactionMode.TRANSACTIONAL)
                        .lockingMode(LockingMode.PESSIMISTIC)
                        .useSynchronization(true)
                        .transactionManagerLookup(new DummyTransactionManagerLookup())
                    .build();
    }
    
    @Produces @CacheName("rendezvous")
    public ClusteredCache<String, RendezvousEntity> newClusteredCache(){
        Cache<String, RendezvousEntity> cache = cacheManager.getCache("rendezvous");
        return new ClusteredCache<>(cache);
    }
    
    @Produces @CacheName("rendezvous") Cache<String, RendezvousEntity> newCache(){
        return cacheManager.getCache("rendezvous");
    }
    
}
