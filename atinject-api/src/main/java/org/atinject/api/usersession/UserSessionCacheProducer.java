package org.atinject.api.usersession;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.cache.ClusteredCacheManager;
import org.infinispan.Cache;

public class UserSessionCacheProducer {
    
    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @CacheName("session")
    public ClusteredCache<String, UserSession> newClusteredCache(){
        Cache<String, UserSession> cache = cacheManager.getCache("session");
        return new ClusteredCache<>(cache);
    }
}
