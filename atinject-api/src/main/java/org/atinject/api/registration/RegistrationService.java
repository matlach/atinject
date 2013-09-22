package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RegistrationService extends Service {

    @Inject UserService userService;
    
    @Inject UserCredentialService userCredentialService;
    
    @Inject RegistrationEventFactory registrationEventFactory;
    
    @Inject GuestUsernamePasswordGenerator guestUsernamePasswordGenerator;
    
    @Inject Event<GuestRegistered> guestRegisteredEvent;
    @Inject Event<UserRegistered> userRegisteredEvent;
    
    public boolean isUsernameAvailable(String username){
        UserEntity user = userService.getUserByName(username);
        if (user == null){
            return true;
        }
        return false;
    }
    
    public UserEntity registerAsGuest(){
        String username = guestUsernamePasswordGenerator.generateUsername();
        String password = guestUsernamePasswordGenerator.generatePassword();
        return registerAsGuest(username, password);
    }
    
    public UserEntity registerAsGuest(String username, String password){
    	
    	UserEntity user = userService.addUser(username);
    	userCredentialService.setUserCredential(user.getId(), username, password);
    	
    	GuestRegistered event = registrationEventFactory
    			.newGuestRegistered()
    			.setUser(user);
    	guestRegisteredEvent.fire(event);
        return user;
    }
    
    public UserEntity register(String userId, String username, String password){
    	
    	UserEntity user = userService.getUser(userId);
    	userCredentialService.setUserCredential(userId, username, password);
    	
        UserRegistered event = registrationEventFactory
                .newUserRegistered()
                .setUser(user);
            userRegisteredEvent.fire(event);
            
        return user;
    }
    
}
