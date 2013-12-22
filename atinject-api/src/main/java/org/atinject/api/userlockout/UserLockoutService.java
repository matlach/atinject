package org.atinject.api.userlockout;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.authentication.event.AuthenticationFailed;
import org.atinject.api.user.UserService;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.userlockout.event.UserLocked;
import org.atinject.api.userlockout.event.UserUnlocked;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserLockoutService extends Service {

    // Threshold 5 times, duration 30 minutes, reset duration 5 minutes.
    
    @Inject UserService userService;
    
    @Inject UserCredentialService userCredentialService;
    
    @Inject Event<UserLocked> userLockedEvent;
    
    @Inject Event<UserUnlocked> userUnlockedEvent;
    
    public boolean isUserLocked(String username){
        return false;
    }
    
    public void lockUser(String username) {
        UserCredentialEntity userCredential = userCredentialService.getUserCredential(username);
        userLockedEvent.fire(new UserLocked());
    }
    
    public void unlockUser(String username) {
        userUnlockedEvent.fire(new UserUnlocked());
    }
    
    public void onAuthenticationFailed(@Observes AuthenticationFailed event){
        // increment authentication failed count
    }
}
