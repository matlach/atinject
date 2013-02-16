package org.atinject.core.cache.event;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;

public class CacheStarted extends Event
{
    private static final long serialVersionUID = 1L;

    private CacheStartedEvent event;
    
    public CacheStarted(){
        
    }

    public CacheStartedEvent getEvent()
    {
        return event;
    }

    public CacheStarted setEvent(CacheStartedEvent event)
    {
        this.event = event;
        return this;
    }
    
}
