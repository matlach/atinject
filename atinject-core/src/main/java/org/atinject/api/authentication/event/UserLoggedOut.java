package org.atinject.api.authentication.event;

import org.atinject.api.session.Session;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;

public class UserLoggedOut extends Event {

    private static final long serialVersionUID = 1L;

    private Session session;
    
    private UserEntity user;
    
    public UserLoggedOut(){
        super();
    }
    
    public Session getSession(){
        return session;
    }

    public UserLoggedOut setSession(Session session){
        this.session = session;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserLoggedOut setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    
}
