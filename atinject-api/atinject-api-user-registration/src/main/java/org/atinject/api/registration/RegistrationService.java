package org.atinject.api.registration;

import java.util.UUID;

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

    public boolean isUsernameAvailable(String username) {
        UserCredentialEntity userCredential = userCredentialService.getUserCredential(username);
        if (userCredential == null) {
            return true;
        }
        return false;
    }

    public UserEntity registerAsGuest() {
        String username = guestUsernamePasswordGenerator.generateUsername();
        String password = guestUsernamePasswordGenerator.generatePassword();
        return registerAsGuest(username, password);
    }

    public UserEntity registerAsGuest(String username, String password) {

        UserEntity user = userService.addUser(username);
        UserCredentialEntity userCredential = userCredentialService.setUserCredential(user.getId(), username, password);

        GuestRegistered event = registrationEventFactory
                .newGuestRegistered()
                .setUser(user)
                .setUserCredential(userCredential);
        guestRegisteredEvent.fire(event);
        return user;
    }

    public UserEntity register(UUID userId, String username, String password) {

        UserEntity user = userService.getUser(userId);
        UserCredentialEntity userCredential = userCredentialService.setUserCredential(userId, username, password);

        UserRegistered event = registrationEventFactory
                .newUserRegistered()
                .setUser(user)
                .setUserCredential(userCredential);
        userRegisteredEvent.fire(event);

        return user;
    }

}
