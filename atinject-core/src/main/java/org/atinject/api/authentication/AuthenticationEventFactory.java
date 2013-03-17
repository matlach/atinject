package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.core.cdi.CDI;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class AuthenticationEventFactory extends EventFactory {

    public UserLoggedIn newUserLoggedIn(){
        return CDI.select(UserLoggedIn.class).get();
    }
    
    public UserLoggedOut newUserLoggedOut(){
        return CDI.select(UserLoggedOut.class).get();
    }
}
