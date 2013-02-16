package org.atinject.api.session.event;

import org.atinject.api.session.Session;
import org.atinject.core.event.Event;

public class SessionClosed extends Event
{
    private static final long serialVersionUID = 1L;

    private Session session;
    
    public SessionClosed(){
        super();
    }

    public Session getSession()
    {
        return session;
    }

    public SessionClosed setSession(Session session)
    {
        this.session = session;
        return this;
    }
}
