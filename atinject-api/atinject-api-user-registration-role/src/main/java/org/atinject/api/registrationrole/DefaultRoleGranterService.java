package org.atinject.api.registrationrole;

import javax.decorator.Delegate;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.registration.RegistrationService;
import org.atinject.api.registration.event.GuestRegistered;
import org.atinject.api.registration.event.UserRegistered;
import org.atinject.api.role.enumeration.DefaultRoles;
import org.atinject.api.userrole.UserRoleService;
import org.atinject.core.tiers.Service;

@Service
public abstract class DefaultRoleGranterService {

    @Inject @Delegate RegistrationService registrationService;
    
    @Inject UserRoleService userRoleService;
    
    public void onGuestRegistered(@Observes GuestRegistered event) {
        userRoleService.grantUserRole(event.getRegistratedUser().getUser().getId(), DefaultRoles.GUEST);
    }
    
    public void onUserRegistered(@Observes UserRegistered event) {
        userRoleService.revokeUserRole(event.getRegistratedUser().getUser().getId(), DefaultRoles.GUEST);
        userRoleService.grantUserRole(event.getRegistratedUser().getUser().getId(), DefaultRoles.REGISTERED);
    }
    
}
