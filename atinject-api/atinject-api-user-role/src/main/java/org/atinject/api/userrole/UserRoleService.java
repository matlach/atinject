package org.atinject.api.userrole;

import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.role.RoleService;
import org.atinject.api.role.enumeration.Roles;
import org.atinject.api.user.validation.ExistingUserId;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserRoleService {

    @Inject private RoleService roleService;
    @Inject private UserRoleEntityFactory factory;
    @Inject @Named("user-role") private DistributedCache<UUID, UserRolesEntity> userRoleCache;
    
    public UserRolesEntity getUserRole(@NotNull UUID userId) {
        return userRoleCache.get(userId)
        		.orElse(addUserRole(userId));
    }
    
    protected UserRolesEntity addUserRole(@NotNull @ExistingUserId UUID userId) {
    	UserRolesEntity userRoles = factory.newUserRoles().setUserId(userId);
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
