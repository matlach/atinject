package org.atinject.core.infinispan;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.startup.Startup;
import org.infinispan.manager.EmbeddedCacheManager;

@Startup
@ApplicationScoped
public class CacheManagerShutdownHook
{

    @Inject
    private EmbeddedCacheManager cacheManager;
    
    @PreDestroy
    public void cleanUp()
    {
        cacheManager.stop();
    }
    
}
