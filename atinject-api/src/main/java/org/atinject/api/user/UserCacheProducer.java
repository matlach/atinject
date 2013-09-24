package org.atinject.api.user;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.cache.ClusteredCacheManager;
import org.atinject.core.cdi.Named;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

public class UserCacheProducer {
    
    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @Named("user")
    public Configuration newCacheConfiguration() {
        return new ConfigurationBuilder()
                    .clustering()
                        .cacheMode(CacheMode.DIST_SYNC)
                    .dataContainer()
                    	.persistence()
                    		.addSingleFileStore()
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
    
    @Produces @Named("user")
    public ClusteredCache<String, UserEntity> newClusteredCache(){
        Cache<String, UserEntity> cache = cacheManager.getCache("user");
        return new ClusteredCache<>(cache);
    }
    
    @Produces @Named("user") Cache<String, UserEntity> newCache(){
        return cacheManager.getCache("user");
    }
}
