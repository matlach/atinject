package org.atinject.core.cache;

import org.atinject.core.logging.LoggerFactory;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStopped;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.slf4j.Logger;

@Listener
public class ClusteredCacheManagerListener
{
    private Logger logger = LoggerFactory.getLogger(ClusteredCacheManagerListener.class);
    
    @CacheStarted
    public void onCacheStarted(CacheStartedEvent event){
        logger.info("cache '{}' started", event.getCacheName());
    }
    
    @CacheStopped
    public void onCacheStopped(CacheStoppedEvent event){
        logger.info("cache '{}' stopped", event.getCacheName());
    }
    
    @ViewChanged
    public void onViewChanged(ViewChangedEvent event){
        logger.info("view changed '{}'", event.getNewMembers());
    }
}
