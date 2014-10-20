package org.atinject.core.cache;

import org.infinispan.distribution.DistributionManager;
import org.infinispan.filter.KeyValueFilter;
import org.infinispan.metadata.Metadata;

public class DistributedCache<K, V> extends ClusteredCache<K, V> {

    @Override
    DistributedCache<K, V> withCache(org.infinispan.Cache<K, V> cache) {
        super.withCache(cache);
        return this;
    }
    
    public DistributionManager getDistributionManager() {
        return super.cache.getDistributionManager();
    }
    
    public final KeyValueFilter<K, V> affineKeyValueFilter = (K key, V value, Metadata metadata) -> getDistributionManager().getLocality(key).isLocal();
}
