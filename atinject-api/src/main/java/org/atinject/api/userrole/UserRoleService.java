package org.atinject.api.userrole;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.role.RoleService;
import org.atinject.api.user.UserService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserRoleService extends Service {

    @Inject UserService userService;
    @Inject RoleService roleService;
    @Inject UserRoleEntityFactory factory;
    @Inject UserRoleCacheStore userRoleCache;
    
    public UserRolesEntity getUserRole(UUID userId){
        UserRolesEntity userRoles = userRoleCache.getUserRole(userId);
        if (userRoles != null) {
            return userRoles;
        }
        userRoles = factory.newUserRoles().setUserId(userId);
        userRoleCache.putUserRoles(userRoles);
        return userRoles;
    }
    
    public void grantUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        if (userRoles.containsRole(role)){
            // throw
        }
        userRoles.addRole(role);
        userRoleCache.putUserRoles(userRoles);
    }
    
    public void revokeUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        if (! userRoles.containsRole(role)){
            // throw
        }
        userRoles.removeRole(role);
        userRoleCache.putUserRoles(userRoles);
    }

}
