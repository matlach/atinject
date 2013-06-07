package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class UserCacheStore extends CacheStore
{

    @Inject @CacheName("user") private ClusteredCache<String, UserEntity> cache;
    
    public UserEntity getUser(String userId){
        return cache.get(userId);
    }
    
    /**
     * Note : this will perform a map reduce search
     */
    public UserEntity getUserByName(String name){
        return null;
    }

    public void lockUser(String userId)
    {
        cache.lock(userId);
    }
    
    public void putUser(UserEntity user)
    {
        cache.put(user.getId(), user);
    }
    
    public void removeUser(UserEntity user){
        cache.remove(user.getId());
    }

}
