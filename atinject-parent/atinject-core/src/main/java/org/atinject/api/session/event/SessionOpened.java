package org.atinject.api.session.event;

import org.atinject.api.session.Session;

public class SessionOpened
{
    private Session session;
    
    public SessionOpened(){
        super();
    }

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }
    
}
