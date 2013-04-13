package org.atinject.api.userrole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class UserRoleCacheStore extends CacheStore {

    @Inject @CacheName("user-role") private ClusteredCache<String, UserRolesEntity> cache;
    
    public UserRolesEntity getUserRole(String userId){
        return cache.get(userId);
    }
}
