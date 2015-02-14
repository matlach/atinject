package org.atinject.api.authorization;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.role.enumeration.DefaultRoles;
import org.atinject.api.rolepermission.RolePermissionService;
import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.api.user.UserService;
import org.atinject.api.userpermission.UserPermissionService;
import org.atinject.api.userpermission.UserPermissions;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.Service;

@Service
public class AuthorizationService {
    
	@Inject
    private UserService userService;
    
	@Inject
    private UserRoleService userRoleService;
    
	@Inject
    private UserPermissionService userPermissionService;

    @Inject
    private RolePermissionService rolePermissionService;

    public boolean isPermitted(UUID userId, String permission) {
        Set<String> permissions = new HashSet<>();
        // get user roles
        UserRolesEntity userRoles = userRoleService.getUserRole(userId);

        // get user roles permissions
        for (String role : userRoles.getRoles()) {
            // TODO batch
            RolePermissions rolePermissions = rolePermissionService.getRolePermissions(role).get();
            permissions.addAll(rolePermissions.getPermissions());
        }

        // get user permissions
        UserPermissions userPermissions = userPermissionService.getUserPermissions(userId);
        if (userPermissions != null) {
            permissions.addAll(userPermissions.getPermissions());
        }

        // TODO result should be cached to avoid performance impact
        return permissions.contains(permission);
    }

    public boolean isGuest(UserSession session) {
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isGuest(userRoles);
    }

    public boolean isGuest(UserRolesEntity userRoles) {
        return userRoles.containsRole(DefaultRoles.GUEST);
    }

    public boolean isRegistered(UserSession session) {
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isRegistered(userRoles);
    }

    public boolean isRegistered(UserRolesEntity userRoles) {
        return userRoles.containsRole(DefaultRoles.REGISTERED);
    }

    public boolean isAdmin(UserSession session) {
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isAdmin(userRoles);
    }

    public boolean isAdmin(UserRolesEntity userRoles) {
        return userRoles.containsRole(DefaultRoles.ADMIN);
    }

}
