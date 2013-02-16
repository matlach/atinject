package org.atinject.core.cache.event;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;

public class CacheStopped extends Event
{
    private static final long serialVersionUID = 1L;

    private CacheStoppedEvent event;
    
    public CacheStopped(){
        
    }

    public CacheStoppedEvent getEvent()
    {
        return event;
    }

    public CacheStopped setEvent(CacheStoppedEvent event)
    {
        this.event = event;
        return this;
    }
    
}
