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
        return userRoleCache.getUserRole(userId);
    }
    
    public void addUserRole(String userId, Role role){
        
    }
    
    public void removeUserRole(String userId, Role role){
        
    }
    
}
