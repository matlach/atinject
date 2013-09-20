package org.atinject.api.authorization;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.role.enumeration.SimpleRoles;
import org.atinject.api.rolepermission.RolePermissionService;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.user.UserService;
import org.atinject.api.userpermission.UserPermissionService;
import org.atinject.api.userpermission.UserPermissions;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthorizationService extends Service {
    @Inject UserService userService;
    @Inject UserRoleService userRoleService;
    @Inject UserPermissionService userPermissionService;
    
    @Inject RolePermissionService rolePermissionService;
    
    public boolean isPermitted(String userId, String permission){
    	Set<String> permissions = new HashSet<>();
    	// get user roles
    	UserRolesEntity userRoles = userRoleService.getUserRole(userId);
    	
    	// get user roles permissions
    	for (String role : userRoles.getRoles()) {
    		// TODO batch
    		RolePermissions rolePermissions = rolePermissionService.getRolePermissions(role);
    		permissions.addAll(rolePermissions.getPermissions());
    	}
    	
    	// get user permissions
    	UserPermissions userPermissions = userPermissionService.getUserPermissions(userId);
    	if (userPermissions != null)
    	{
    		permissions.addAll(userPermissions.getPermissions());
    	}
    	
    	// TODO result should be cached to avoid performance impact
    	return permissions.contains(permission);
    }
    
    public boolean isGuest(UserSession session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isGuest(userRoles);
    }
    
    public boolean isGuest(UserRolesEntity userRoles){
        return userRoles.containsRole(SimpleRoles.GUEST);
    }
    
    public boolean isRegistered(UserSession session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isRegistered(userRoles);
    }
    
    public boolean isRegistered(UserRolesEntity userRoles){
        return userRoles.containsRole(SimpleRoles.REGISTERED);
    }
    
    public boolean isAdmin(UserSession session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isAdmin(userRoles);
    }
    
    public boolean isAdmin(UserRolesEntity userRoles){
        return userRoles.containsRole(SimpleRoles.ADMIN);
    }
    
    public void onGuest(@Observes GuestRegistered event){
        // add guest role
        userRoleService.grantUserRole(event.getUser().getId(), SimpleRoles.GUEST);
    }
    
    public void onUserRegistered(@Observes UserRegistered event){
    	// TODO optimize here: lock, get, remove, add, put
        // remove guest role
        userRoleService.revokeUserRole(event.getUser().getId(), SimpleRoles.GUEST);
        
        // add registered role
        userRoleService.grantUserRole(event.getUser().getId(), SimpleRoles.REGISTERED);
    }
}
