package org.atinject.api.registration.event;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;

public class UserRegistered extends Event {

    private static final long serialVersionUID = 1L;

    private UserEntity user;
    
    public UserRegistered(){
        super();
    }

    public UserEntity getUser() {
        return user;
    }

    public UserRegistered setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    
}
