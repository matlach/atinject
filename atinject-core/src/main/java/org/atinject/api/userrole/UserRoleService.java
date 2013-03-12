package org.atinject.api.userrole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.role.RoleService;
import org.atinject.api.user.UserService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserRoleService extends Service {

    @Inject private UserService userService;
    @Inject private RoleService roleService;
    
}
