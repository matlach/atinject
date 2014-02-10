package org.atinject.core.cache;

import org.infinispan.distribution.DistributionManager;

public class DistributedCache<K, V> extends ClusteredCache<K, V> {

    @Override
    DistributedCache<K, V> withCache(org.infinispan.Cache<K, V> cache) {
        super.withCache(cache);
        return this;
    }
    
    public DistributionManager getDistributionManager() {
        return super.cache.getDistributionManager();
    }
    
}
