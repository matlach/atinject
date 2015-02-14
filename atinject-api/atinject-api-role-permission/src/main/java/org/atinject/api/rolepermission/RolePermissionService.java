package org.atinject.api.rolepermission;

import java.util.Optional;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.permission.entity.Permissions;
import org.atinject.api.role.RoleService;
import org.atinject.api.role.enumeration.Roles;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.rolepermission.event.PermissionGrantedToRole;
import org.atinject.api.rolepermission.event.PermissionRevokedToRole;
import org.atinject.api.rolepermission.exception.RolePermissionException;
import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class RolePermissionService {

    @Inject
    RoleService roleService;
    
    @Inject
    PermissionService permissionService;
    
    @Inject @Named("rolepermission")
    ReplicatedCache<String, RolePermissions> rolePermissionCache;

    @Inject
    Event<PermissionGrantedToRole> permissionGrantedToRoleEvent;
    
    @Inject
    Event<PermissionRevokedToRole> permissionRevokedToRoleEvent;

    public <R extends Enum<?> & Roles> Optional<RolePermissions> getRolePermissions(R role) {
    	return getRolePermissions(role.name());
    }
    
    public Optional<RolePermissions> getRolePermissions(String role) {
        return rolePermissionCache.get(role);
    }
    
    public <R extends Enum<?> & Roles> RolePermissions addRolePermissions(R role) {
    	return addRolePermissions(role.name());
    }
    
    public RolePermissions addRolePermissions(String role) {
        if (getRolePermissions(role) != null) {
            throw new RolePermissionException("role '" + role + "' already exists");
        }
        RolePermissions rolePermissions = new RolePermissions().setRole(role);
        rolePermissionCache.put(role, rolePermissions);
        return rolePermissions;
    }

    public <R extends Enum<?> & Roles, P extends Enum<?> & Roles> boolean isPermitted(R role, P permission) {
    	return isPermitted(role.name(), permission.name());
    }
    
    public boolean isPermitted(String role, String permission) {
        if (!roleService.isRole(role)) {
            throw new RolePermissionException("role '" + role + "' do not exists");
        }
        if (!permissionService.isPermission(permission)) {
            throw new RolePermissionException("permission '" + permission + "' do not exists");
        }
        RolePermissions rolePermissions = getRolePermissions(role)
        		.orElseThrow(() -> new RolePermissionException("role '" + role + "' has no permission"));
        return rolePermissions.hasPermission(permission);
    }

    public <R extends Enum<?> & Roles, P extends Enum<?> & Permissions> void grantPermissionToRole(R role, P permission) {
    	grantPermissionToRole(role.name(), permission.name());
    }
    
    public void grantPermissionToRole(String role, String permission) {
        RolePermissions rolePermissions = getRolePermissions(role).get();
        grantPermissionToRole(rolePermissions, permission);
    }
    
    public void grantPermissionToRole(RolePermissions rolePermissions, String permission) {
        rolePermissions.addPermission(permission);
        rolePermissionCache.put(rolePermissions.getRole(), rolePermissions);
        permissionGrantedToRoleEvent.fire(new PermissionGrantedToRole());
    }

    public <R extends Enum<?> & Roles, P extends Enum<?> & Roles> void revokePermissionToRole(R role, P permission) {
    	revokePermissionToRole(role.name(), permission.name());
    }
    
    public void revokePermissionToRole(String role, String permission) {
        // lock, get, revoke, put
        permissionRevokedToRoleEvent.fire(new PermissionRevokedToRole());
    }
}
