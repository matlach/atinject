package org.atinject.api.registration;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

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
    
    @Inject Event<UserRegistered> userRegisteredEvent;
    
    public boolean isUsernameAvailable(String username){
        UserEntity user = userService.getUserByName(username);
        if (user == null){
            return true;
        }
        return false;
    }
    
    public UserEntity registerAsGuest(){
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        return registerAsGuest(username, password);
    }
    
    public UserEntity registerAsGuest(String username, String password){
    	
    	UserEntity user = userService.addUser(username);
    	userCredentialService.setUserCredential(user.getId(), username, password);
    	
        UserRegistered userRegistered = registrationEventFactory
            .newUserRegistered()
            .setUser(user);
        userRegisteredEvent.fire(userRegistered);
        return user;
    }
    
    public UserEntity register(){
        throw new RuntimeException("not implemented");
    }
    
}
