package org.atinject.core.cache.event;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

public class ViewChanged extends Event
{
    private static final long serialVersionUID = 1L;
    
    private ViewChangedEvent event;
    
    public ViewChanged(){
        
    }

    public ViewChangedEvent getEvent()
    {
        return event;
    }

    public ViewChanged setEvent(ViewChangedEvent event)
    {
        this.event = event;
        return this;
    }
    
}
