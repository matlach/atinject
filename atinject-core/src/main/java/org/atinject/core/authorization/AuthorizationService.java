package org.atinject.core.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.role.enumeration.Roles;
import org.atinject.api.session.Session;
import org.atinject.api.user.UserService;
import org.atinject.api.userpermission.UserPermissionService;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthorizationService extends Service {
    // this service should provides caching to lower the performance impact of applying authorization ?
    @Inject private UserService userService;
    @Inject private UserRoleService userRoleService;
    @Inject private UserPermissionService userPermissionService;
    
    // FIXME if enum is "extended",
    // userRoles.getRoles().contains(Roles.GUEST) is error prone.
    // as hashcode / equals methods are not overridable in an enum
    // see maybe : http://stackoverflow.com/questions/3564139/how-to-override-the-final-equals-method-in-java-enums
    
    public boolean isGuest(Session session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isGuest(userRoles);
    }
    
    public boolean isGuest(UserRolesEntity userRoles){
        return userRoles.getRoles().contains(Roles.GUEST);
    }
    
    public boolean isRegistered(Session session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isRegistered(userRoles);
    }
    
    public boolean isRegistered(UserRolesEntity userRoles){
        return userRoles.getRoles().contains(Roles.REGISTERED);
    }
    
    public boolean isAdmin(Session session){
        UserRolesEntity userRoles = userRoleService.getUserRole(session.getUserId());
        return isAdmin(userRoles);
    }
    
    public boolean isAdmin(UserRolesEntity userRoles){
        return userRoles.getRoles().contains(Roles.ADMIN);
    }
    
    public void onGuest(@Observes GuestRegistered event){
        // add guest role
        userRoleService.addUserRole(event.getUser().getId(), Roles.GUEST);
    }
    
    public void onUserRegistered(@Observes UserRegistered event){
        // remove guest role
        userRoleService.removeUserRole(event.getUser().getId(), Roles.GUEST);
        
        // add registered role
        userRoleService.addUserRole(event.getUser().getId(), Roles.REGISTERED);
    }
}
