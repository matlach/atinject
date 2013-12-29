package org.atinject.core.cache;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.atinject.core.cdi.CDI;
import org.atinject.core.cdi.Named;

public class CacheProducer {

    @Inject private CacheManager cacheManager;
    
    @Produces @Named
    public <K, V> LocalCache<K, V> newLocalCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        if (cache == null) {
            throw new ExceptionInInitializerError("cannot find cache named '" + named.value() + "'");
        }
        return CDI.select(LocalCache.class).get().withCache(cache);
    }
    
    @Produces @Named
    public <K, V> DistributedCache<K, V> newDistributedCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        if (cache == null) {
            throw new ExceptionInInitializerError("cannot find cache named '" + named.value() + "'");
        }
        return CDI.select(DistributedCache.class).get().withCache(cache);
    }
    
    @Produces @Named
    public <K, V> ReplicatedCache<K, V> newReplicatedCache(InjectionPoint ip){
        Named named = ip.getAnnotated().getAnnotation(Named.class);
        org.infinispan.Cache<K, V> cache = (org.infinispan.Cache<K, V>) cacheManager.getCache(named.value());
        if (cache == null) {
            throw new ExceptionInInitializerError("cannot find cache named '" + named.value() + "'");
        }
        return CDI.select(ReplicatedCache.class).get().withCache(cache);
    }
    
}
