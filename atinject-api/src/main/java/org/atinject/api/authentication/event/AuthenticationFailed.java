package org.atinject.api.authentication.event;

import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.event.Event;

public class AuthenticationFailed extends Event {

    private static final long serialVersionUID = 1L;

    private UserSession userSession;
    
    private UserCredentialEntity userCredential;
    
    public AuthenticationFailed(){
        super();
    }
    
    public UserSession getSession(){
        return userSession;
    }

    public AuthenticationFailed setUserSession(UserSession userSession){
        this.userSession = userSession;
        return this;
    }

    public UserCredentialEntity getUserCredential(){
        return userCredential;
    }

    public AuthenticationFailed setUserCredential(UserCredentialEntity userCredential){
        this.userCredential = userCredential;
        return this;
    }
    
}
