package org.atinject.api.authentication;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.nullanalysis.NonNull;

public interface AuthenticationService {

	UserEntity login(@NonNull UserSession session, @NonNull String username, @NonNull String password);
	
	void logout(UserSession session);
}
