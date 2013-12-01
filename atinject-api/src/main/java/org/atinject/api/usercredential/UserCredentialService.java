package org.atinject.api.usercredential;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.registration.PasswordDigester;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserCredentialService extends Service {

    @Inject private UserCredentialCacheStore userCredentialCacheStore;
    
    @Inject private PasswordDigester passwordDigester;
    
    public UserCredentialEntity getUserCredential(String username){
        return userCredentialCacheStore.getUserCredential(username);
    }
    
    public UserCredentialEntity setUserCredential(UUID userId, String username, String password){
        userCredentialCacheStore.lock(username);
        UserCredentialEntity userCredential = new UserCredentialEntity()
            .setUserId(userId)
            .setUsername(username)
            .setPasswordHash(passwordDigester.digest(password));
        userCredentialCacheStore.put(userCredential);
        return userCredential;
    }
    
    public UserCredentialEntity changePassword(String username, String newPassword){
        userCredentialCacheStore.lock(username);
        UserCredentialEntity userCredential = userCredentialCacheStore.getUserCredential(username);
        return changePassword(userCredential, newPassword);
    }
    
    /**
     * Note : we assume userCredential should have been locked before
     */
    public UserCredentialEntity changePassword(UserCredentialEntity userCredential, String newPassword){
        String hashedPassword = passwordDigester.digest(newPassword);
        
        if (userCredential.getPasswordHash().equals(hashedPassword)) {
            throw new RuntimeException("password is the same");
        }
        
        userCredential.setPasswordHash(hashedPassword);
        
        userCredentialCacheStore.put(userCredential);
        
        return userCredential;
    }
    
}
