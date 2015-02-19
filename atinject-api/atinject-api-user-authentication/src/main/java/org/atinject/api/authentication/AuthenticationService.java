package org.atinject.api.authentication;

import javax.validation.constraints.NotNull;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;

public interface AuthenticationService {

    UserEntity login(@NotNull UserSession session, @NotNull String username, @NotNull String password);

    void logout(UserSession session);
}
