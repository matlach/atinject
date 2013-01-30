package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserAdded;
import org.atinject.core.concurrent.Asynchronous;
import org.atinject.core.distevent.Distributed;
import org.atinject.core.event.BaseEvent;
import org.atinject.core.transaction.InfinispanTransactional;
import org.slf4j.Logger;

@InfinispanTransactional
@ApplicationScoped
public class UserServiceImpl implements UserService{
    
    @Inject
    private Logger logger;
    
    @Inject
    private UserCacheStore userCacheStore;
    
    @Inject @Distributed
    private Event<BaseEvent> userAddedEvent;
    
    public void fireUserAdded(UserAdded event)
    {
        userAddedEvent.fire(event);
    }
    
    @Asynchronous
    public void async()
    {
        System.out.print("async");
    }
    
    public void onUserAdded(@Observes UserAdded event)
    {
        System.out.println("yay");
    }
    
    @Asynchronous
    public void onUserAddedAsync(@Observes UserAdded event)
    {
        System.out.println("yay");
    }

    @Override
    public UserEntity getUser(String userUUID)
    {
        return userCacheStore.getUser(userUUID);
    }

    @Override
    public UserEntity addUser(String name, String password)
    {
        String userUUID = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        user.setUuid(userUUID);
        user.setName(name);
        user.setPassword(password);
        
        userCacheStore.lockUser(userUUID);
        userCacheStore.putUser(user);
        
        return user;
    }
    
    @Override
    public void updateUser(UserEntity user){
        userCacheStore.lockUser(user.getUuid());
        userCacheStore.putUser(user);
    }

    @Override
    public void removeUser(String userUUID)
    {
        userCacheStore.lockUser(userUUID);
        UserEntity user = userCacheStore.getUser(userUUID);
        if (user == null){
            throw new RuntimeException();
        }
        userCacheStore.removeUser(user);
    }
    
    @Override
    public void removeUser(UserEntity user){
        userCacheStore.lockUser(user.getUuid());
        userCacheStore.removeUser(user);
    }
}
