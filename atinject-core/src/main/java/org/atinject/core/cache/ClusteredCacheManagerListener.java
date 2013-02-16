package org.atinject.core.cache;

import org.atinject.core.cache.event.CacheStarted;
import org.atinject.core.cache.event.CacheStopped;
import org.atinject.core.cache.event.ViewChanged;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.logging.LoggerFactory;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.slf4j.Logger;

@Listener
public class ClusteredCacheManagerListener
{
    private Logger logger = LoggerFactory.getLogger(ClusteredCacheManagerListener.class);
    
    @org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted
    public void onCacheStarted(CacheStartedEvent event){
        logger.info("cache '{}' started", event.getCacheName());
        BeanManagerExtension.fireEvent(new CacheStarted().setEvent(event));
    }
    
    @org.infinispan.notifications.cachemanagerlistener.annotation.CacheStopped
    public void onCacheStopped(CacheStoppedEvent event){
        logger.info("cache '{}' stopped", event.getCacheName());
        BeanManagerExtension.fireEvent(new CacheStopped().setEvent(event));
    }
    
    @org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged
    public void onViewChanged(ViewChangedEvent event){
        logger.info("view changed '{}'", event.getNewMembers());
        BeanManagerExtension.fireEvent(new ViewChanged().setEvent(event));
    }
}
