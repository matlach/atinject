package org.atinject.api.authorization;

import java.util.Optional;
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
public class SuperAdminInitializer {

	@Inject
	private SuperAdminCredentialProvider superAdminCredentialProvider;
	
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
    
    @PostConstruct
    public <R extends Enum<?> & Roles, P extends Enum<?> & Permissions> void initialize() {
        // ensure super admin user / user credential exists
        UserEntity superAdminUser = getSuperAdminUser();
        
        // ensure super admin user has the super admin role
        UserRolesEntity superAdminRoles = userRoleService.getUserRole(superAdminUser.getId());
        if (superAdminRoles.containsRole(DefaultRoles.SUPER_ADMIN)) {
            userRoleService.grantUserRole(superAdminUser.getId(), DefaultRoles.SUPER_ADMIN);
        }
        
        // ensure super admin role has all the permission
        RolePermissions superAdminRolePermissions = rolePermissionService.getRolePermissions(DefaultRoles.SUPER_ADMIN)
        		.orElse(rolePermissionService.addRolePermissions(DefaultRoles.SUPER_ADMIN));
        Set<P> staticPermissions = permissionService.getAllStaticPermission();
        for (P permission : staticPermissions) {
            if (! superAdminRolePermissions.hasPermission(permission)) {
                rolePermissionService.grantPermissionToRole(DefaultRoles.SUPER_ADMIN, permission);
            }
        }
    }
    
    public UserEntity getSuperAdminUser() {
    	Optional<UserCredentialEntity> superAdminCredential = userCredentialService.getUserCredential(superAdminCredentialProvider.getUsername());
    	if (superAdminCredential.isPresent()) {
    		return userService.getUser(superAdminCredential.get().getUserId()).get();
    	}
    	return addSuperAdminUser();
    }
    
    public UserEntity addSuperAdminUser() {
    	UserEntity superAdminUser = userService.addUser(superAdminCredentialProvider.getUsername());
        userCredentialService.setUserCredential(superAdminUser.getId(), superAdminCredentialProvider.getUsername(), superAdminCredentialProvider.getPassword());
        return superAdminUser;
    }

}
