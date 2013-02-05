package org.atinject.api.session;

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

    public void setSession(Session session)
    {
        this.session = session;
    }
}
