package org.atinject.core.cache.event;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;

public class CacheStopped extends Event
{
    private static final long serialVersionUID = 1L;

    private String cacheName;
    
    public CacheStopped(){
        
    }

    public String getCacheName()
    {
        return cacheName;
    }

    public CacheStopped setEvent(CacheStoppedEvent event)
    {
        this.cacheName = event.getCacheName();
        return this;
    }
    
}
