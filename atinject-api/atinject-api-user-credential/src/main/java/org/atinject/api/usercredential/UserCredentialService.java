package org.atinject.api.usercredential;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserCredentialService {

    @Inject @Named("user-credential") DistributedCache<String, UserCredentialEntity> userCredentialCacheStore;
    
    @Inject private SaltGenerator saltGenerator;
    
    @Inject private PasswordDigester passwordDigester;
    
    public Optional<UserCredentialEntity> getUserCredential(String username){
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
    
    public UserCredentialEntity changePassword(String username, String currentPassword, String newPassword){
        userCredentialCacheStore.lock(username);
        UserCredentialEntity userCredential = userCredentialCacheStore.get(username).orElseThrow(() -> new NullPointerException());
        return changePassword(userCredential, currentPassword, newPassword);
    }
    
    /**
     * Note : we assume userCredential should have been locked before
     */
    public UserCredentialEntity changePassword(UserCredentialEntity userCredential, String currentPassword, String newPassword){
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
