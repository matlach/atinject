package org.atinject.api.rolepermission;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.role.RoleService;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.rolepermission.event.PermissionGrantedToRole;
import org.atinject.api.rolepermission.event.PermissionRevokedToRole;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RolePermissionService extends Service {

    @Inject RoleService roleService;
    @Inject PermissionService permissionService;
    @Inject RolePermissionCache rolePermissionCache;
    
    @Inject Event<PermissionGrantedToRole> permissionGrantedToRoleEvent;
    @Inject Event<PermissionRevokedToRole> permissionRevokedToRoleEvent;
    
    public RolePermissions getRolePermissions(String role){
    	return rolePermissionCache.getRolePermissions(role);
    }
    
    public boolean isPermitted(String role, String permission) {
        if (! roleService.isRole(role)){
        	throw new NullPointerException("role '" + role + "' do not exists");
        }
        if (! permissionService.isPermission(permission)) {
        	throw new NullPointerException("permission '" + permission + "' do not exists");
        }
        RolePermissions rolePermissions = getRolePermissions(role);
        if (rolePermissions == null) {
        	throw new NullPointerException("role '" + role + "' has no permission");
        }
    	return rolePermissions.hasPermission(permission);
    }
    
    public void grantPermissionToRole(String role, String permission) {
    	
    	// lock, get, grant, put
    	permissionGrantedToRoleEvent.fire(new PermissionGrantedToRole());
    }
    
    public void revokePermissionToRole(String role, String permission) {
    	// lock, get, revoke, put
    	permissionRevokedToRoleEvent.fire(new PermissionRevokedToRole());
    }
}
