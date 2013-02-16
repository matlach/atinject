package org.atinject.api.session.event;

import org.atinject.api.session.Session;
import org.atinject.core.event.Event;

public class SessionOpened extends Event
{
    private static final long serialVersionUID = 1L;
    
    private Session session;
    
    public SessionOpened(){
        super();
    }

    public Session getSession()
    {
        return session;
    }

    public SessionOpened setSession(Session session)
    {
        this.session = session;
        return this;
    }
    
}
