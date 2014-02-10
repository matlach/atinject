package org.atinject.api.rolepermission;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.role.RoleService;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.rolepermission.event.PermissionGrantedToRole;
import org.atinject.api.rolepermission.event.PermissionRevokedToRole;
import org.atinject.api.rolepermission.exception.RolePermissionException;
import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RolePermissionService extends Service {

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

    public RolePermissions getRolePermissions(String role) {
        return rolePermissionCache.get(role);
    }
    
    public RolePermissions addRolePermissions(String role) {
        if (getRolePermissions(role) != null) {
            throw new RolePermissionException("role '" + role + "' already exists");
        }
        RolePermissions rolePermissions = new RolePermissions().setRole(role);
        rolePermissionCache.put(role, rolePermissions);
        return rolePermissions;
    }

    public boolean isPermitted(String role, String permission) {
        if (!roleService.isRole(role)) {
            throw new RolePermissionException("role '" + role + "' do not exists");
        }
        if (!permissionService.isPermission(permission)) {
            throw new RolePermissionException("permission '" + permission + "' do not exists");
        }
        RolePermissions rolePermissions = getRolePermissions(role);
        if (rolePermissions == null) {
            throw new RolePermissionException("role '" + role + "' has no permission");
        }
        return rolePermissions.hasPermission(permission);
    }

    public void grantPermissionToRole(String role, String permission) {
        RolePermissions rolePermissions = getRolePermissions(role);
        grantPermissionToRole(rolePermissions, permission);
    }
    
    public void grantPermissionToRole(RolePermissions rolePermissions, String permission) {
        rolePermissions.addPermission(permission);
        rolePermissionCache.put(rolePermissions.getRole(), rolePermissions);
        permissionGrantedToRoleEvent.fire(new PermissionGrantedToRole());
    }

    public void revokePermissionToRole(String role, String permission) {
        // lock, get, revoke, put
        permissionRevokedToRoleEvent.fire(new PermissionRevokedToRole());
    }
}
