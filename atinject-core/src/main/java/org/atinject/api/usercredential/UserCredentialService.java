package org.atinject.api.usercredential;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserCredentialService extends Service {

    @Inject UserCredentialCacheStore userCredentialCacheStore;
    
    public UserCredentialEntity getUserCredential(String username){
        return userCredentialCacheStore.getUserCredential(username);
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
        if (userCredential.getPassword().equals(newPassword)) {
            // same password, throw;
        }
        userCredential.setPassword(newPassword);
        
        userCredentialCacheStore.put(userCredential);
        
        return userCredential;
    }
    
}
