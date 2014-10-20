package org.atinject.api.userpermission;

import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.user.UserService;
import org.atinject.core.tiers.Service;

@Service
public class UserPermissionService {

    @Inject private UserService userService;
    @Inject private PermissionService permissionService;
    
    public UserPermissions getUserPermissions(UUID userId){
        return null;
    }
}
