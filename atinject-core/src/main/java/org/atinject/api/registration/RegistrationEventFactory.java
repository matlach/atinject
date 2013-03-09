package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.registration.event.UserRegistered;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class RegistrationEventFactory extends EventFactory {

    public UserRegistered newUserRegistered(){
        return BeanManagerExtension.getReference(UserRegistered.class);
    }
}
