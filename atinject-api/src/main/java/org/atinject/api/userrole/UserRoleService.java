package org.atinject.api.userrole;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.role.RoleService;
import org.atinject.api.user.UserService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserRoleService extends Service {

    @Inject UserService userService;
    @Inject RoleService roleService;
    @Inject UserRoleEntityFactory factory;
    @Inject @Named("user-role") private DistributedCache<UUID, UserRolesEntity> userRoleCache;
    
    public UserRolesEntity getUserRole(UUID userId){
        UserRolesEntity userRoles = userRoleCache.get(userId);
        if (userRoles != null) {
            return userRoles;
        }
        userRoles = factory.newUserRoles().setUserId(userId);
        userRoleCache.put(userId, userRoles);
        return userRoles;
    }
    
    public void grantUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        if (userRoles.containsRole(role)){
            // throw
        }
        userRoles.addRole(role);
        userRoleCache.put(userId, userRoles);
    }
    
    public void revokeUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        if (! userRoles.containsRole(role)){
            // throw
        }
        userRoles.removeRole(role);
        userRoleCache.put(userId, userRoles);
    }

}
