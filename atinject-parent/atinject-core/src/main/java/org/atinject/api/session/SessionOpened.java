package org.atinject.api.session;

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
