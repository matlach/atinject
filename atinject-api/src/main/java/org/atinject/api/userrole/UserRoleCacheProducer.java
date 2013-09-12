package org.atinject.api.userrole;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.api.userrole.entity.UserRolesEntity;
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

public class UserRoleCacheProducer {
    
    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @CacheName("user-role")
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
    
    @Produces @CacheName("user-role")
    public ClusteredCache<String, UserRolesEntity> newClusteredCache(){
        Cache<String, UserRolesEntity> cache = cacheManager.getCache("user-role");
        return new ClusteredCache<>(cache);
    }
    
}
