package org.atinject.api.registration.event;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;

public class GuestRegistered extends Event {

    private static final long serialVersionUID = 1L;

    private UserEntity user;
    
    public GuestRegistered(){
        super();
    }

    public UserEntity getUser() {
        return user;
    }

    public GuestRegistered setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    
}