package org.atinject.api.usercredential;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserCredentialService extends Service {

    @Inject @Named("user-credential") DistributedCache<String, UserCredentialEntity> userCredentialCacheStore;
    
    @Inject private SaltGenerator saltGenerator;
    
    @Inject private PasswordDigester passwordDigester;
    
    public UserCredentialEntity getUserCredential(String username){
        return userCredentialCacheStore.get(username);
    }
    
    public UserCredentialEntity setUserCredential(UUID userId, String username, String password){
        userCredentialCacheStore.lock(username);
        String salt = saltGenerator.get();
        String saltedHash = passwordDigester.digest(salt + password);
        UserCredentialEntity userCredential = new UserCredentialEntity()
            .setUserId(userId)
            .setUsername(username)
            .setSalt(salt)
            .setHash(saltedHash);
        userCredentialCacheStore.put(username, userCredential);
        return userCredential;
    }
    
    public UserCredentialEntity changePassword(String username, String newPassword){
        userCredentialCacheStore.lock(username);
        UserCredentialEntity userCredential = userCredentialCacheStore.get(username);
        return changePassword(userCredential, newPassword);
    }
    
    /**
     * Note : we assume userCredential should have been locked before
     */
    public UserCredentialEntity changePassword(UserCredentialEntity userCredential, String newPassword){
        String saltedHash = passwordDigester.digest(userCredential.getSalt() + newPassword);
        
        if (userCredential.getHash().equals(saltedHash)) {
            throw new RuntimeException("password is the same");
        }
        
        String newSalt = saltGenerator.get();
        String newSaltedHash = passwordDigester.digest(newSalt + newPassword);
        userCredential.setSalt(newSalt).setHash(newSaltedHash);
        
        userCredentialCacheStore.put(userCredential.getUsername(), userCredential);
        
        return userCredential;
    }
    
}
