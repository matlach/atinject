package org.atinject.api.user.event;

import org.atinject.api.session.Session;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;

public class UserLoggedIn extends Event
{
    private static final long serialVersionUID = 1L;

    private Session session;
    
    private UserEntity user;

    public UserLoggedIn(){
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

    public UserEntity getUser()
    {
        return user;
    }

    public void setUser(UserEntity user)
    {
        this.user = user;
    }
    
}
