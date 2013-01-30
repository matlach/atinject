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
    public UserEntity getUser(String userUUID){
        return cache.get(userUUID);
    }

    @Override
    public void lockUser(String userUUID)
    {
        cache.lock(userUUID);
    }
    
    @Override
    public void putUser(UserEntity user)
    {
        cache.put(user.getUuid(), user);
    }
    
    @Override
    public void removeUser(UserEntity user){
        cache.remove(user.getUuid());
    }

}
