package org.atinject.api.registration.event;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.event.Event;

public class UserRegistered extends Event {

    private static final long serialVersionUID = 1L;

    private UserEntity user;
    private UserCredentialEntity userCredential;
    
    public UserEntity getUser() {
        return user;
    }

    public UserRegistered setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    
    public UserCredentialEntity getUserCredential() {
        return userCredential;
    }

    public UserRegistered setUserCredential(UserCredentialEntity userCredential) {
        this.userCredential = userCredential;
        return this;
    }
}
