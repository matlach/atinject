package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.InfinispanCache;
import org.atinject.core.tiers.AbstractCacheStore;

@ApplicationScoped
public class UserCacheStoreImpl extends AbstractCacheStore implements UserCacheStore
{

    @Inject @CacheName("user")
    private InfinispanCache<String, UserEntity> cache;
    
    @Override
    public UserEntity getUser(String userId){
        return cache.get(userId);
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
        cache.lock(userId);
    }
    
    @Override
    public void putUser(UserEntity user)
    {
        cache.put(user.getId(), user);
    }
    
    @Override
    public void removeUser(UserEntity user){
        cache.remove(user.getId());
    }

}
