package org.atinject.api.userpreference;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.api.userpreference.entity.UserPreferencesEntity;
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

public class UserPreferenceCacheProducer {

    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @Named("user-preferences")
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
    
    @Produces @Named("user-preferences")
    public ClusteredCache<String, UserPreferencesEntity> newClusteredCache(){
        Cache<String, UserPreferencesEntity> cache = cacheManager.getCache("user-preferences");
        return new ClusteredCache<>(cache);
    }
    
}
