package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class UserCacheStore extends CacheStore
{

    @Inject @Named("user") private DistributedCache<UUID, UserEntity> cache;
    
    public UserEntity getUser(UUID userId){
        return cache.get(userId);
    }
    
    /**
     * Note : this will perform a map reduce search
     */
    public UserEntity getUserByName(String name){
        return null;
    }

    public void lockUser(UUID userId) {
        cache.lock(userId);
    }
    
    public void putUser(UserEntity user) {
        cache.put(user.getId(), user);
    }
    
    public void removeUser(UserEntity user) {
        cache.remove(user.getId());
    }

}
