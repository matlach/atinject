package org.atinject.core.infinispan;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

public class EmbeddedCacheManagerProducer {

    @Inject
    private InfinispanExtension infinispanExtension;
    
    @Produces
    public EmbeddedCacheManager embeddedCacheManager()
    {
        return infinispanExtension.getCacheManager();
    }
    
    @SuppressWarnings("rawtypes")
    @Produces @CacheName
    public Cache cache(InjectionPoint ip)
    {
        String cacheName = ip.getAnnotated().getAnnotation(CacheName.class).value();
        return infinispanExtension.getCacheManager().getCache(cacheName);
    }
}
