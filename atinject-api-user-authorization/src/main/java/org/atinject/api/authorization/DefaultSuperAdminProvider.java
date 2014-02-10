package org.atinject.api.authorization;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.role.enumeration.DefaultRoles;
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
    public void initialize() {
        // ensure super admin user / user credential exists
        UserCredentialEntity superAdminCredential = userCredentialService.getUserCredential(getUsername());
        if (superAdminCredential == null) {
            UserEntity superAdminUser = userService.addUser(getUsername());
            superAdminCredential = userCredentialService.setUserCredential(superAdminUser.getId(), getUsername(), getPassword());
        }
        
        // ensure super admin user has the super admin role
        UserRolesEntity superAdminRoles = userRoleService.getUserRole(superAdminCredential.getUserId());
        if (superAdminRoles.containsRole(DefaultRoles.SUPER_ADMIN)) {
            userRoleService.grantUserRole(superAdminRoles, DefaultRoles.SUPER_ADMIN);
        }
        
        // ensure super admin role has all the permission
        RolePermissions superAdminPermissions = rolePermissionService.getRolePermissions(DefaultRoles.SUPER_ADMIN);
        if (superAdminPermissions == null) {
            superAdminPermissions = rolePermissionService.addRolePermissions(DefaultRoles.SUPER_ADMIN);
        }
        for (String permission : permissionService.getAllPermission()) {
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
