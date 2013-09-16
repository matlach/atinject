package org.atinject.api.rolepermission;

import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.role.RoleService;

public class RolePermissionService {

    @Inject RoleService roleService;
    @Inject PermissionService permissionService;
    
    public boolean isPermitted(String role, String privilege){
        return true;
    }
}
