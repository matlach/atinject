package org.atinject.core.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.session.Session;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.enumeration.UserStates;
import org.atinject.api.userpermission.UserPermissionService;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthorizationService extends Service {
    // this service should provides caching to lower the performance impact of applying authorization ?
    @Inject private UserService userService;
    @Inject private UserRoleService userRoleService;
    @Inject private UserPermissionService userPermissionService;
    
    public boolean isGuest(Session session){
        UserEntity user = userService.getUser(session.getUserId());
        return isGuest(user);
    }
    
    public boolean isGuest(UserEntity user){
        return UserStates.isGuest(user.getState());
    }
    
    public boolean isRegistered(Session session){
        UserEntity user = userService.getUser(session.getUserId());
        return isRegistered(user);
    }
    
    public boolean isRegistered(UserEntity user){
        return UserStates.isRegistered(user.getState());
    }
    
    public boolean isAdmin(Session session){
        UserEntity user = userService.getUser(session.getUserId());
        return isAdmin(user);
    }
    
    public boolean isAdmin(UserEntity user){
        return UserStates.isAdmin(user.getState());
    }
    
    public void onGuest(@Observes UserRegistered event){
        // add guest role ? or maybe more permissions ?
    }
    
    public void onUserRegistered(@Observes UserRegistered event){
        // remove guest role ?
        // add registered role ?
    }
}
