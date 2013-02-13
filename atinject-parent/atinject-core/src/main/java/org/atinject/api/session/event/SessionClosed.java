package org.atinject.api.session.event;

import org.atinject.api.session.Session;

public class SessionClosed
{
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
