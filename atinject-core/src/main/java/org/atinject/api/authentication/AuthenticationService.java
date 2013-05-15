package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.api.authentication.exception.WrongPasswordException;
import org.atinject.api.authentication.exception.WrongUsernameException;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.distevent.Distributed;
import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.session.Session;
import org.atinject.core.session.SessionService;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthenticationService extends Service {

    @Inject private UserCredentialService userCredentialService;
    
    @Inject private UserService userService;
    
    @Inject private SessionService sessionService;
    
    @Inject private AuthenticationEventFactory authenticationEventFactory;
    
    @Inject @Distributed private Event<UserLoggedIn> userLoggedInEvent;
    
    @Inject @Distributed private Event<UserLoggedOut> userLoggerOutEvent;
    
    public UserEntity login(@NonNull Session session, @NonNull String username, @NonNull String password){
        UserSession userSession = (UserSession) session;
        if (userSession.getUserId() != null){
            throw new IllegalStateException("session user id is not null");
        }
        UserCredentialEntity userCredential = userCredentialService.getUserCredential(username);
        if (userCredential == null) {
            throw new WrongUsernameException();
        }
        if (! userCredential.getPasswordHash().equals(password)){
            throw new WrongPasswordException();
        }
        
        // update session
        userSession.setUserId(userCredential.getUserId());
        sessionService.updateSession(session);
        
        UserEntity user = userService.getUser(userCredential.getUserId());
        
        UserLoggedIn userLoggedIn = authenticationEventFactory
                .newUserLoggedIn()
                .setSession(session)
                .setUser(user);
        userLoggedInEvent.fire(userLoggedIn);
        
        return user;
    }
    
    public void logout(){
        
    }
}
