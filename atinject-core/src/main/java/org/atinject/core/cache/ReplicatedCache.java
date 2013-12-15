package org.atinject.core.cache;


public class ReplicatedCache<K, V> extends ClusteredCache<K, V> {

    @Override
    ReplicatedCache withCache(org.infinispan.Cache<K, V> cache) {
        super.withCache(cache);
        return this;
    }
}
