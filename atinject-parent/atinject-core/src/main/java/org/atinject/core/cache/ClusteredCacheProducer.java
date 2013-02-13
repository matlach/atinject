package org.atinject.core.cache;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.infinispan.Cache;

public class ClusteredCacheProducer {

    @Inject
    private ClusteredCacheManager cacheManager;

    // TODO is it better to inject a raw type with a producer or inject an application scoped type that extends clustered cache
//    @SuppressWarnings("rawtypes")
//    @Produces
//    public ClusteredCache infinispanCache(InjectionPoint ip)
//    {
//        String cacheName = ip.getAnnotated().getAnnotation(CacheName.class).value();
//        ClusteredCache infinispanCache = new ClusteredCache(cacheManager.getCache(cacheName).getAdvancedCache());
//        return infinispanCache;
//    }
    
    @SuppressWarnings("rawtypes")
    @Produces
    public Cache cache(InjectionPoint ip)
    {
        String cacheName = ip.getAnnotated().getAnnotation(CacheName.class).value();
        return cacheManager.getCache(cacheName);
    }
}
