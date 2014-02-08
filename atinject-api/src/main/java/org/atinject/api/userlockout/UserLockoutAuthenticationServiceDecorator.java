package org.atinject.api.userlockout;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import org.atinject.api.authentication.AuthenticationService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.userlockout.exception.UserLockedException;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.nullanalysis.NonNull;

@Decorator
public abstract class UserLockoutAuthenticationServiceDecorator implements AuthenticationService {

    @Inject @Delegate AuthenticationService authenticationService;
    
    @Inject UserLockoutService userLockoutService;
    
    @Override
    public UserEntity login(@NonNull UserSession session, @NonNull String username, @NonNull String password) {
        if (userLockoutService.isUserLocked(username)) {
            throw new UserLockedException();
        }
        return authenticationService.login(session, username, password);
    }
}
