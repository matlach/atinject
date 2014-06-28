package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class RegistrationEventFactory extends EventFactory {

    public GuestRegistered newGuestRegistered(){
        return new GuestRegistered();
    }
    
    public UserRegistered newUserRegistered(){
        return new UserRegistered();
    }
}
