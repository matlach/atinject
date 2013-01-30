package org.atinject.api.user;

import javax.enterprise.inject.Produces;

import org.atinject.core.cache.CacheName;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

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
}
