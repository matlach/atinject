package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;

@CacheName("user")
@ApplicationScoped
public class UserCacheStore extends ClusteredCache<String, UserEntity>
{
    
    public UserEntity getUser(String userId){
        return get(userId);
    }
    
    /**
     * Note : this will perform a map reduce search
     */
    public UserEntity getUserByName(String name){
        return null;
    }

    public void lockUser(String userId)
    {
        lock(userId);
    }
    
    public void putUser(UserEntity user)
    {
        put(user.getId(), user);
    }
    
    public void removeUser(UserEntity user){
        remove(user.getId());
    }

}
