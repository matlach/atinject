package org.atinject.api.usersession;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.cache.ClusteredCacheManager;
import org.atinject.core.cdi.Named;
import org.infinispan.Cache;

public class UserSessionCacheProducer {
    
    @Inject private ClusteredCacheManager cacheManager;
    
    @Produces @Named("session")
    public ClusteredCache<String, UserSession> newClusteredCache(){
        Cache<String, UserSession> cache = cacheManager.getCache("session");
        return new ClusteredCache<>(cache);
    }
}
