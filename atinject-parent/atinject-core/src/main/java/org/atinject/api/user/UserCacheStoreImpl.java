package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;

@CacheName("user")
@ApplicationScoped
public class UserCacheStoreImpl extends ClusteredCache<String, UserEntity> implements UserCacheStore
{
    
    @Override
    public UserEntity getUser(String userId){
        return get(userId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByName(String name){
        return null;
    }

    @Override
    public void lockUser(String userId)
    {
        lock(userId);
    }
    
    @Override
    public void putUser(UserEntity user)
    {
        put(user.getId(), user);
    }
    
    @Override
    public void removeUser(UserEntity user){
        remove(user.getId());
    }

}
