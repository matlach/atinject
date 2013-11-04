package org.atinject.core.cache;

import org.infinispan.Cache;

public class ReplicatedCache<K, V> extends ClusteredCache<K, V> {

    public ReplicatedCache(Cache<K, V> cache) {
        super(cache);
    }

}
