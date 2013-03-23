package org.atinject.core.cache.event;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;

public class CacheStarted extends Event
{
    private static final long serialVersionUID = 1L;

    private String cacheName;
    
    public CacheStarted(){
        
    }

    public String getCacheName()
    {
        return cacheName;
    }

    public CacheStarted setEvent(CacheStartedEvent event)
    {
        this.cacheName = event.getCacheName();
        return this;
    }
    
}
