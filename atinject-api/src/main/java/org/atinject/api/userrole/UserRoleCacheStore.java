package org.atinject.api.userrole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class UserRoleCacheStore extends CacheStore {

    @Inject @Named("user-role") private DistributedCache<String, UserRolesEntity> cache;
    
    public UserRolesEntity getUserRole(String userId){
        return cache.get(userId);
    }
    
    public void putUserRoles(UserRolesEntity userRoles){
        cache.put(userRoles.getUserId(), userRoles);
    }
    
    public void removeUserRoles(UserRolesEntity userRoles){
        cache.remove(userRoles.getUserId());
    }
}
