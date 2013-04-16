package org.atinject.api.userrole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.role.RoleService;
import org.atinject.api.role.enumeration.Role;
import org.atinject.api.user.UserService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserRoleService extends Service {

    @Inject private UserService userService;
    @Inject private RoleService roleService;
    @Inject private UserRoleCacheStore userRoleCache;
    
    public UserRolesEntity getUserRole(String userId){
        UserRolesEntity userRoles = userRoleCache.getUserRole(userId);
        if (userRoles != null){
            return userRoles;
        }
        userRoles = new UserRolesEntity().setUserId(userId);
        userRoleCache.putUserRoles(userRoles);
        return userRoles;
    }
    
    public void addUserRole(String userId, Role role){
        UserRolesEntity userRoles = getUserRole(userId);
        userRoles.addRole(role);
        userRoleCache.putUserRoles(userRoles);
    }
    
    public void removeUserRole(String userId, Role role){
        UserRolesEntity userRoles = getUserRole(userId);
        userRoles.removeRole(role);
        userRoleCache.putUserRoles(userRoles);
    }

}
