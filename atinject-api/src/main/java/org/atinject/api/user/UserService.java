package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserIdCollided;
import org.atinject.api.user.event.UserRelocated;
import org.atinject.core.tiers.Service;
import org.slf4j.Logger;

@ApplicationScoped
public class UserService extends Service{
    
    @Inject Logger logger;
    
    @Inject UserCacheStore userCacheStore;
    
    @Inject UserEntityFactory userEntityFactory;
    
    @Inject UserIdGenerator userIdGenerator;
    
    @Inject Event<UserIdCollided> userIdCollidedEvent;
    
    /**
     * get the user by delegating to {@link UserCacheStore#getUser(String)}
     */
    public UserEntity getUser(UUID userId){
        return userCacheStore.getUser(userId);
    }
    
    /**
     * get the user by delegating to {@link UserCacheStore#getUserByName(String)}
     */
    public UserEntity getUserByName(String name){
        return userCacheStore.getUserByName(name);
    }
    
    /**
     * lock the user by delegating to {@link UserCacheStore#lockUser(String)}
     */
    public void lockUser(UUID userId){
        userCacheStore.lockUser(userId);
    }
    
    /**
     * create, lock and add new user with random userId and specified name and password
     * Note : userId key will be locked
     */
    public UserEntity addUser(String name) {
        UUID userId = userIdGenerator.generateUserId();
        // be extra careful here as everything is based on user id
        // TODO any better way to do this ? what about locking ?
        if (userCacheStore.getUser(userId) != null){
            // an extraordinary event just happened
            // try again
            userIdCollidedEvent.fire(new UserIdCollided().setUserId(userId));
            return addUser(name);
        }
        UserEntity user = userEntityFactory.newUserEntity()
                .setId(userId)
                .setName(name);
        lockUser(userId);
        addUser(user);
        return user;
    }
    
    // TODO relocate, is this the correct word / concept ??
    @Inject Event<UserRelocated> userRelocatedEvent;
    
    public void relocateUser(UUID userId) {
        UserEntity user = userCacheStore.getUser(userId);
        userCacheStore.removeUser(user);
        
        // TODO generate another id
        UUID newUserId = userId;
        user.setId(userId);
        userCacheStore.putUser(user);
        
        userRelocatedEvent.fire(new UserRelocated().setOldUserId(userId).setNewUserId(newUserId));
    }
    
    /**
     * add the user by delegating to {@link UserCacheStore#putUser(UserEntity)}
     * Note : it is assumed that userId has been locked before
     */
    public void addUser(UserEntity user) {
        userCacheStore.putUser(user);
    }
    
    public void updateUserName(UUID userId, String name) {
        UserEntity user = getUser(userId);
        updateUserName(user, name);
    }
    
    public void updateUserName(UserEntity user, String name) {
        user.setName(name);
        updateUser(user);
    }
    
    /**
     * update the user by delegating to {@link UserCacheStore#putUser(UserEntity)}
     * Note : it is assumed that userId has been locked before
     */
    public void updateUser(UserEntity user) {
        userCacheStore.putUser(user);
    }

    /**
     * lock, get, and remove user
     */
    public UserEntity removeUser(UUID userId) {
        lockUser(userId);
        UserEntity user = userCacheStore.getUser(userId);
        removeUser(user);
        return user;
    }
    
    /**
     * Note : it is assumed that userId has been locked before
     */
    public void removeUser(UserEntity user) {
        if (user == null) {
            throw new NullPointerException("user");
        }
        userCacheStore.removeUser(user);
    }
}
