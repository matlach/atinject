package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserAdded;
import org.atinject.api.user.event.UserLoggedIn;
import org.atinject.core.concurrent.Asynchronous;
import org.atinject.core.distevent.Distributed;
import org.atinject.core.tiers.Service;
import org.slf4j.Logger;

@ApplicationScoped
public class UserService extends Service{
    
    @Inject
    private Logger logger;
    
    @Inject
    private UserCacheStore userCacheStore;
    
    @Inject
    private SessionService sessionService;
    
    @Inject @Distributed
    private Event<UserAdded> userAddedEvent;
    
    @Inject @Distributed
    private Event<UserLoggedIn> userLoggedInEvent;
    
    @Inject
    private UserConfiguration userConfiguration;
    
    public void fireUserAdded(UserAdded event)
    {
        userAddedEvent.fire(event);
    }
    
    public void login(Session session, String name, String password){
        UserEntity user = userCacheStore.getUserByName(name);
        if (user == null){
            throw new RuntimeException("wrong username");
        }
        if (! user.getPassword().equals(password)){
            throw new RuntimeException("wrong password");
        }
        
        // update session
        session.setUserId(user.getId());
        sessionService.updateSession(session);
        
        UserLoggedIn userLoggedIn = new UserLoggedIn();
        userLoggedIn.setSession(session);
        userLoggedIn.setUser(user);
        userLoggedInEvent.fire(userLoggedIn);
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

    public UserEntity getUser(String userUUID)
    {
        return userCacheStore.getUser(userUUID);
    }

    public UserEntity addUser(String name, String password)
    {
        String userUUID = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        user.setId(userUUID);
        user.setName(name);
        user.setPassword(password);
        
        userCacheStore.lockUser(userUUID);
        userCacheStore.putUser(user);
        
        return user;
    }
    
    public void updateUser(UserEntity user){
        userCacheStore.lockUser(user.getId());
        userCacheStore.putUser(user);
    }

    public void removeUser(String userId)
    {
        userCacheStore.lockUser(userId);
        UserEntity user = userCacheStore.getUser(userId);
        if (user == null){
            throw new RuntimeException();
        }
        userCacheStore.removeUser(user);
    }
    
    public void removeUser(UserEntity user){
        userCacheStore.lockUser(user.getId());
        userCacheStore.removeUser(user);
    }
}
