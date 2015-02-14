package org.atinject.api.user;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserIdCollided;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserService {
    
    @Inject @Named("user")
    private DistributedCache<UUID, UserEntity> userCacheStore;
    
    @Inject
    private UserEntityFactory userEntityFactory;
    
    @Inject
    private UserIdGenerator userIdGenerator;
    
    @Inject
    private Event<UserIdCollided> userIdCollidedEvent;
    
    public Optional<UserEntity> getUser(UUID userId){
        return userCacheStore.get(userId);
    }
    
    public void lockUser(UUID userId){
        userCacheStore.lock(userId);
    }
    
    /**
     * create, lock and add new user with random userId and specified name and password
     * Note : userId key will be locked
     */
    public UserEntity addUser(String name) {
        UUID userId = userIdGenerator.generateUserId();
        // be extra careful here as everything is based on user id
        // TODO any better way to do this ? what about locking ?
        if (userCacheStore.get(userId).isPresent()){
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
    
    /**
     * add the user by delegating to {@link UserCacheStore#putUser(UserEntity)}
     * Note : it is assumed that userId has been locked before
     */
    public void addUser(UserEntity user) {
        userCacheStore.put(user.getId(), user);
    }
    
    public void updateUserName(UUID userId, String name) {
        updateUserName(
        		getUser(userId)
        		.orElseThrow(() -> new NullPointerException()), name);
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
        userCacheStore.put(user.getId(), user);
    }

    /**
     * lock, get, and remove user
     */
    public UserEntity removeUser(UUID userId) {
        lockUser(userId);
        UserEntity user = userCacheStore.get(userId).orElseThrow(() -> new NullPointerException());
        removeUser(user);
        return user;
    }
    
    /**
     * Note : it is assumed that userId has been locked before
     */
    public void removeUser(@NotNull UserEntity user) {
        userCacheStore.remove(user.getId());
    }
}
