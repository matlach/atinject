package org.atinject.api.userlockout;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.authentication.AuthenticationService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.userlockout.exception.UserLockedException;
import org.atinject.api.usersession.UserSession;

@Decorator
public abstract class UserLockoutAuthenticationServiceDecorator implements AuthenticationService {

    @Inject @Delegate
    private AuthenticationService authenticationService;
    
    @Inject
    private UserLockoutService userLockoutService;
    
    @Override
    public UserEntity login(@NotNull UserSession session, @NotNull String username, @NotNull String password) {
        if (userLockoutService.isUserLocked(username)) {
            throw new UserLockedException();
        }
        return authenticationService.login(session, username, password);
    }
}
