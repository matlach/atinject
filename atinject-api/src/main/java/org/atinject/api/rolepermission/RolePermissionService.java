package org.atinject.api.rolepermission;

import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.permission.entity.Permission;
import org.atinject.api.role.RoleService;
import org.atinject.api.role.enumeration.Role;

public class RolePermissionService {

    @Inject RoleService roleService;
    @Inject PermissionService permissionService;
    
    public boolean isPermitted(Role role, Permission privilege){
        return true;
    }
}
