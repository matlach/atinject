package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.authentication.event.AuthenticationFailed;
import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.api.authentication.exception.WrongPasswordException;
import org.atinject.api.authentication.exception.WrongUsernameException;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.PasswordDigester;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.distevent.Distributed;
import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.session.SessionService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthenticationServiceImpl extends Service implements AuthenticationService {

    @Inject
    private UserCredentialService userCredentialService;

    @Inject
    private UserService userService;

    @Inject
    private SessionService sessionService;

    @Inject
    private AuthenticationEventFactory authenticationEventFactory;

    @Inject
    private Event<AuthenticationFailed> authenticationFailedEvent;

    @Inject
    private PasswordDigester passwordDigester;
    
    @Inject
    @Distributed
    private Event<UserLoggedIn> userLoggedInEvent;

    @Inject
    @Distributed
    private Event<UserLoggedOut> userLoggerOutEvent;

    /**
     * @throws WrongUsernameException
     * @throws WrongPasswordException
     */
    @Override
    public UserEntity login(@NonNull UserSession session, @NonNull String username, @NonNull String password) {
        if (session.getUserId() != null) {
            throw new IllegalStateException("session user id is not null");
        }
        UserCredentialEntity userCredential = userCredentialService.getUserCredential(username);
        if (userCredential == null) {
            throw new WrongUsernameException();
        }
        
        String saltedHash = passwordDigester.digest(userCredential.getSalt() + password);
        if (!userCredential.getHash().equals(saltedHash)) {
            AuthenticationFailed event = authenticationEventFactory.newAuthenticationFailed()
                    .setUserSession(session)
                    .setUserCredential(userCredential);
            // TODO listen this event to implement a lock user credential feature, apply on user credential ?
            authenticationFailedEvent.fire(event);
            throw new WrongPasswordException();
        }

        // update session
        session.setUserId(userCredential.getUserId());
        sessionService.updateSession(session);

        UserEntity user = userService.getUser(userCredential.getUserId());

        UserLoggedIn userLoggedIn = authenticationEventFactory
                .newUserLoggedIn()
                .setSession(session)
                .setUser(user);
        userLoggedInEvent.fire(userLoggedIn);

        return user;
    }

    @Override
    public void logout(UserSession session) {
        if (session.getUserId() == null) {
            // nothing to do
            return;
        }
        UserEntity user = userService.getUser(session.getUserId());
        UserLoggedOut userLoggedOut = authenticationEventFactory
                .newUserLoggedOut()
                .setSession(session)
                .setUser(user);
        userLoggerOutEvent.fire(userLoggedOut);
    }

}
