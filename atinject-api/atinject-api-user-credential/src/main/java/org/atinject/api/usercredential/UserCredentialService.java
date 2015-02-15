package org.atinject.api.usercredential;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.user.validation.ExistingUserId;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.usercredential.validation.ExistingUsername;
import org.atinject.api.usercredential.validation.NewPassword;
import org.atinject.api.usercredential.validation.NewUsername;
import org.atinject.api.usercredential.validation.NonExistingUsername;
import org.atinject.api.usercredential.validation.ComplexPassword;
import org.atinject.api.usercredential.validation.AcceptableUsername;
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
    
    public UserCredentialEntity addUserCredential(
    		@NotNull @ExistingUserId UUID userId,
    		@NotNull @NonExistingUsername @AcceptableUsername String username,
    		@NotNull @ComplexPassword String password) {
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
    
    public void changeUsernameAndPassword(String username, String password, String newUsername, String newPassword) {
    	changeUsername(username, newUsername);
    	changePassword(newUsername, password, newPassword);
    }
    
    @NewUsername
    public void changeUsername(
    		@NotNull @ExistingUsername String username,
    		@NotNull @NonExistingUsername @AcceptableUsername String newUsername) {
    	userCredentialCacheStore.lockAll(username, newUsername);
    	UserCredentialEntity userCredential = getUserCredential(username).get();
    	userCredentialCacheStore.remove(username);
    	userCredential.setUsername(newUsername);
    	userCredentialCacheStore.put(newUsername, userCredential);
    }
    
    @NewPassword
    public UserCredentialEntity changePassword(
    		@NotNull @ExistingUsername String username,
    		@NotNull String password,
    		@NotNull @ComplexPassword String newPassword) {
        userCredentialCacheStore.lock(username);
        
        UserCredentialEntity userCredential = userCredentialCacheStore.get(username).orElseThrow(() -> new NullPointerException());
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
