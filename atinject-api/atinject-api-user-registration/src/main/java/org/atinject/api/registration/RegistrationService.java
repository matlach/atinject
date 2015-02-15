package org.atinject.api.registration;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.tiers.Service;

@Service
public class RegistrationService {

    @Inject
    private UserService userService;

    @Inject
    private UserCredentialService userCredentialService;

    @Inject
    private RegistrationEventFactory registrationEventFactory;

    @Inject
    private GuestUsernamePasswordGenerator guestUsernamePasswordGenerator;

    @Inject
    private Event<GuestRegistered> guestRegisteredEvent;
    
    @Inject
    private Event<UserRegistered> userRegisteredEvent;

    public RegistratedUser registerAsGuest() {
        String username = guestUsernamePasswordGenerator.generateUsername();
        String password = guestUsernamePasswordGenerator.generatePassword();
        return registerAsGuest(username, password);
    }

    public RegistratedUser registerAsGuest(String username, String password) {
        UserEntity user = userService.addUser(username);
        UserCredentialEntity userCredential = userCredentialService.setUserCredential(user.getId(), username, password);

        RegistratedUser registratedUser = new RegistratedUser();
        registratedUser.setUser(user);
        registratedUser.setUserCredential(userCredential);
        
        GuestRegistered event = registrationEventFactory
                .newGuestRegistered()
                .setRegistratedUser(registratedUser);
        guestRegisteredEvent.fire(event);
        return registratedUser;
    }

    public RegistratedUser registerFromGuest(String username, String password, String newUsername, String newPassword) {
    	UserCredentialEntity userCredential = userCredentialService.getUserCredential(username).get();
    	userCredentialService.changeUsernameAndPassword(username, password, newUsername, newPassword);
        UserEntity user = userService.getUser(userCredential.getUserId()).orElseThrow(() -> new NullPointerException());

        RegistratedUser registratedUser = new RegistratedUser();
        registratedUser.setUser(user);
        registratedUser.setUserCredential(userCredential);
        
        UserRegistered event = registrationEventFactory
                .newUserRegistered()
                .setRegistratedUser(registratedUser);
        userRegisteredEvent.fire(event);

        return registratedUser;
    }

}
