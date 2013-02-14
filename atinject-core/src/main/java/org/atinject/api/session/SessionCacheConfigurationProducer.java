package org.atinject.api.session;

import javax.enterprise.inject.Produces;

import org.atinject.core.cache.CacheName;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

public class SessionCacheConfigurationProducer
{
    @CacheName("session")
    @Produces
    public Configuration newCacheConfiguration() {
        return new ConfigurationBuilder()
                    .clustering()
                        //.cacheMode(CacheMode.REPL_SYNC) // FIXME MapReduceTask need to work as well on Local/Replicated cache
                        .cacheMode(CacheMode.DIST_SYNC)
                    .transaction()
                        .transactionMode(TransactionMode.TRANSACTIONAL)
                        .lockingMode(LockingMode.OPTIMISTIC)
                        .useSynchronization(true)
                        .transactionManagerLookup(new DummyTransactionManagerLookup())
                    .build();
    }
}
