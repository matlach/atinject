package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.authentication.event.AuthenticationFailed;
import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class AuthenticationEventFactory extends EventFactory {

    public AuthenticationFailed newAuthenticationFailed() {
        return new AuthenticationFailed();
    }
    
    public UserLoggedIn newUserLoggedIn(){
        return new UserLoggedIn();
    }
    
    public UserLoggedOut newUserLoggedOut(){
        return new UserLoggedOut();
    }
}
