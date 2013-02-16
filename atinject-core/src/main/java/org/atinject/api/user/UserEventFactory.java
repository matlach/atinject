package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.event.UserAdded;
import org.atinject.api.user.event.UserLoggedIn;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class UserEventFactory extends EventFactory
{

    public UserAdded newUserAdded(){
        return BeanManagerExtension.getReference(UserAdded.class);
    }
    
    public UserLoggedIn newUserLoggedIn(){
        return BeanManagerExtension.getReference(UserLoggedIn.class);
    }
}
