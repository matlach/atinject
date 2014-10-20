package org.atinject.api.authorization;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.permission.entity.Permissions;
import org.atinject.api.role.enumeration.DefaultRoles;
import org.atinject.api.role.enumeration.Roles;
import org.atinject.api.rolepermission.RolePermissionService;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.startup.Startup;

@Startup
@ApplicationScoped
public class DefaultSuperAdminProvider implements SuperAdminProvider {

    @Inject
    private UserCredentialService userCredentialService;
    
    @Inject
    private UserService userService;
    
    @Inject
    private UserRoleService userRoleService;
    
    @Inject
    private RolePermissionService rolePermissionService;
    
    @Inject
    private PermissionService permissionService;
    
    public static final String SUPER_ADMIN_USERNAME = "admin";
    public static final String SUPER_ADMIN_PASSWORD = "admin";
    
    @PostConstruct
    public <R extends Enum<?> & Roles, P extends Enum<?> & Permissions> void initialize() {
        // ensure super admin user / user credential exists
        UserCredentialEntity superAdminCredential = userCredentialService.getUserCredential(getUsername());
        if (superAdminCredential == null) {
            UserEntity superAdminUser = userService.addUser(getUsername());
            superAdminCredential = userCredentialService.setUserCredential(superAdminUser.getId(), getUsername(), getPassword());
        }
        
        // ensure super admin user has the super admin role
        UserRolesEntity superAdminRoles = userRoleService.getUserRole(superAdminCredential.getUserId());
        if (superAdminRoles.containsRole(DefaultRoles.SUPER_ADMIN)) {
            userRoleService.grantUserRole(superAdminCredential.getUserId(), DefaultRoles.SUPER_ADMIN);
        }
        
        // ensure super admin role has all the permission
        RolePermissions superAdminPermissions = rolePermissionService.getRolePermissions(DefaultRoles.SUPER_ADMIN);
        if (superAdminPermissions == null) {
            superAdminPermissions = rolePermissionService.addRolePermissions(DefaultRoles.SUPER_ADMIN);
        }
        Set<P> staticPermissions = permissionService.getAllStaticPermission();
        for (P permission : staticPermissions) {
            if (! superAdminPermissions.hasPermission(permission)) {
                rolePermissionService.grantPermissionToRole(DefaultRoles.SUPER_ADMIN, permission);
            }
        }
    }

    @Override
    public String getUsername() {
        return SUPER_ADMIN_USERNAME;
    }

    @Override
    public String getPassword() {
        return SUPER_ADMIN_PASSWORD;
    }
}
