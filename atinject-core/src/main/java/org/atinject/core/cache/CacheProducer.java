package org.atinject.core.cache;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.atinject.core.cdi.Named;

public class CacheProducer {

    @Inject private CacheManager cacheManager;
    
    @Produces
    public <K, V> LocalCache<K, V> newLocalCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        return new LocalCache<>(cache);
    }
    
    @Produces
    public <K, V> DistributedCache<K, V> newDistributedCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        return new DistributedCache<>(cache);
    }
    
    @Produces
    public <K, V> ReplicatedCache<K, V> newReplicatedCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        return new ReplicatedCache<>(cache);
    }
    
}
