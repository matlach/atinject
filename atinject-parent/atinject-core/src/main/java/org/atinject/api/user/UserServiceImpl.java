package org.atinject.api.user;

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

@InfinispanTransactional
@ApplicationScoped
public class UserServiceImpl implements UserService{
    
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
        return null;
    }
    
    public void addUser(UserEntity user)
    {
        userCacheStore.putUser(user);
    }
}
