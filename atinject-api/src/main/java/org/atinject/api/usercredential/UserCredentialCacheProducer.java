package org.atinject.api.usercredential;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.api.usercredential.entity.UserCredentialEntity;
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

public class UserCredentialCacheProducer {

    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @CacheName("user-credential")
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
    
    @Produces @CacheName("user-credential")
    public ClusteredCache<String, UserCredentialEntity> newClusteredCache(){
        Cache<String, UserCredentialEntity> cache = cacheManager.getCache("user-credential");
        return new ClusteredCache<>(cache);
    }
}
