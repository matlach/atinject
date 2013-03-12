package org.atinject.api.userpermission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.permission.PermissionService;
import org.atinject.api.user.UserService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserPermissionService extends Service {

    @Inject private UserService userService;
    @Inject private PermissionService permissionService;
}
