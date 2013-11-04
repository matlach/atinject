package org.atinject.core.cache;

import org.infinispan.Cache;
import org.infinispan.distribution.DistributionManager;

public class DistributedCache<K, V> extends ClusteredCache<K, V> {

    public DistributedCache(Cache<K, V> cache) {
        super(cache);
    }

    public DistributionManager getDistributionManager() {
        return super.cache.getDistributionManager();
    }
    
}
