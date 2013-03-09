package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class AuthenticationEventFactory extends EventFactory {

    public UserLoggedIn newUserLoggedIn(){
        return BeanManagerExtension.getReference(UserLoggedIn.class);
    }
    
    public UserLoggedOut newUserLoggedOut(){
        return BeanManagerExtension.getReference(UserLoggedOut.class);
    }
}
