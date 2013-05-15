package org.atinject.api.authentication.event;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;
import org.atinject.core.session.Session;

public class UserLoggedIn extends Event
{
    private static final long serialVersionUID = 1L;

    private Session session;
    
    private UserEntity user;

    public UserLoggedIn(){
        super();
    }
    
    public Session getSession(){
        return session;
    }

    public UserLoggedIn setSession(Session session){
        this.session = session;
        return this;
    }

    public UserEntity getUser(){
        return user;
    }

    public UserLoggedIn setUser(UserEntity user){
        this.user = user;
        return this;
    }
    
}
