package org.atinject.core.session.event;

import org.atinject.core.event.Event;
import org.atinject.core.session.Session;

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
