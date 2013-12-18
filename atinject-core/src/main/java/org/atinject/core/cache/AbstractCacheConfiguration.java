package org.atinject.core.cache;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;

public abstract class AbstractCacheConfiguration {

    public abstract ConfigurationBuilder get();
    
    protected void withReplicatedClustering(ConfigurationBuilder configuration) {
        configuration.clustering()
        .cacheMode(CacheMode.REPL_SYNC);
    }
    
    protected void withDistributedClustering(ConfigurationBuilder configuration) {
        configuration.clustering()
        .cacheMode(CacheMode.DIST_SYNC);
    }
    
    protected void withOptimisticTransaction(ConfigurationBuilder configuration) {
        configuration.transaction()
        .transactionMode(TransactionMode.TRANSACTIONAL)
        .lockingMode(LockingMode.OPTIMISTIC)
        .useSynchronization(true)
        .transactionManagerLookup(new DummyTransactionManagerLookup());
    }
    
    protected void withPessimisticTransaction(ConfigurationBuilder configuration) {
        configuration.transaction()
        .transactionMode(TransactionMode.TRANSACTIONAL)
        .lockingMode(LockingMode.PESSIMISTIC)
        .useSynchronization(true)
        .transactionManagerLookup(new DummyTransactionManagerLookup());
    }
    
    protected void withFileBasedPersistence(ConfigurationBuilder configuration) {
        configuration.dataContainer()
        .persistence()
            .addSingleFileStore()
                .async()
                    .enable()
                .purgeOnStartup(true);
    }
}
