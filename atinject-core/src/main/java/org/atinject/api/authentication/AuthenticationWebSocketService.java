package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.authentication.dto.LoginRequest;
import org.atinject.api.authentication.dto.LoginResponse;
import org.atinject.api.authentication.dto.LogoutRequest;
import org.atinject.api.authentication.dto.LogoutResponse;
import org.atinject.api.session.Session;
import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.WebSocketMessage;

@ApplicationScoped
public class AuthenticationWebSocketService extends WebSocketService {

    @Inject private AuthenticationService authenticationService;
    
    @Inject private AuthenticationDTOFactory authenticationDTOFactory;
    
    @Inject private UserAdapter userAdapter;
    
    @WebSocketMessage
    public LoginResponse onLoginRequest(@NonNull LoginRequest request, @NonNull Session session){
        UserEntity userEntity = authenticationService.login(session, request.getUsername(), request.getPasswordHash());
        userAdapter.userEntityToUser(userEntity);
        return authenticationDTOFactory.newLoginResponse();
    }
    
    @WebSocketMessage
    public LogoutResponse onLogoutRequest(LogoutRequest request, Session session){
        authenticationService.logout();
        return authenticationDTOFactory.newLogoutResponse();
    }
    
}
