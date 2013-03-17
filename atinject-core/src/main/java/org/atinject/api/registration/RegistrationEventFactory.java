package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.registration.event.UserRegistered;
import org.atinject.core.cdi.CDI;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class RegistrationEventFactory extends EventFactory {

    public UserRegistered newUserRegistered(){
        return CDI.select(UserRegistered.class).get();
    }
}
