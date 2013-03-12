package org.atinject.core.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.user.UserService;
import org.atinject.api.userpermission.UserPermissionService;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthorizationService extends Service {
    // this service should provides caching to lower the performance impact of applying authorization ?
    @Inject private UserService userService;
    @Inject private UserRoleService userRoleService;
    @Inject private UserPermissionService userPermissionService;
    
    public void onGuest(@Observes UserRegistered event){
        // add guest role
    }
    
    public void onUserRegistered(@Observes UserRegistered event){
        // remove guest role
        // add registered role
    }
}
