package org.atinject.api.registrationrole;

import java.util.UUID;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import org.atinject.api.registration.RegistrationService;
import org.atinject.api.role.enumeration.DefaultRoles;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.userrole.UserRoleService;

@Decorator
public abstract class GrantRoleDecorator implements RegistrationService {

    @Inject @Delegate RegistrationService registrationService;
    
    @Inject UserRoleService userRoleService;
    
    @Override
    public UserEntity registerAsGuest(String username, String password){
        UserEntity user = registrationService.registerAsGuest(username, password);
        userRoleService.grantUserRole(user.getId(), DefaultRoles.GUEST);
        return user;
    }
    
    @Override
    public UserEntity register(UUID userId, String username, String password) {
        UserEntity user = registrationService.register(userId, username, password);
        userRoleService.revokeUserRole(user.getId(), DefaultRoles.GUEST);
        userRoleService.grantUserRole(user.getId(), DefaultRoles.REGISTERED);
        return user;
    }
    
}
