package org.atinject.api.userrole;

import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.role.RoleService;
import org.atinject.api.role.enumeration.Roles;
import org.atinject.api.user.UserService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserRoleService {

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
    
    public <R extends Enum<?> & Roles> void grantUserRole(UUID userId, R role) {
    	grantUserRole(userId, role.name());
    }
    
    public void grantUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        grantUserRole(userRoles, role);
    }
    
    public void grantUserRole(UserRolesEntity userRoles, String role) {
        if (userRoles.containsRole(role)){
            // throw
        }
        userRoles.addRole(role);
        userRoleCache.put(userRoles.getUserId(), userRoles);
    }
    
    public <R extends Enum<?> & Roles> void revokeUserRole(UUID userId, R role) {
    	revokeUserRole(userId, role.name());
    }
    
    public void revokeUserRole(UUID userId, String role){
        UserRolesEntity userRoles = getUserRole(userId);
        revokeUserRole(userRoles, role);
    }
    
    public void revokeUserRole(UserRolesEntity userRoles, String role) {
        if (! userRoles.containsRole(role)){
            // throw
        }
        userRoles.removeRole(role);
        userRoleCache.put(userRoles.getUserId(), userRoles);
    }

}
