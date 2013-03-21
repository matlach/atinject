package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.authentication.event.UserLoggedOut;
import org.atinject.api.authentication.exception.WrongPasswordException;
import org.atinject.api.authentication.exception.WrongUsernameException;
import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usercredential.entity.UserCredentialEntity;
import org.atinject.core.distevent.Distributed;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthenticationService extends Service {

    @Inject private UserCredentialService userCredentialService;
    
    @Inject private UserService userService;
    
    @Inject private SessionService sessionService;
    
    @Inject private AuthenticationEventFactory authenticationEventFactory;
    
    @Inject @Distributed private Event<UserLoggedIn> userLoggedInEvent;
    
    @Inject @Distributed private Event<UserLoggedOut> userLoggerOutEvent;
    
    public UserEntity login(Session session, String username, String password){
        if (session.getUserId() != null){
            throw new IllegalStateException("session user id is not null");
        }
        UserCredentialEntity userCredential = userCredentialService.getUserCredential(username);
        if (userCredential == null) {
            throw new WrongUsernameException();
        }
        if (! userCredential.getPassword().equals(password)){
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
    
    public void logout(){
        
    }
}
