package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.authentication.dto.LoginRequest;
import org.atinject.api.authentication.dto.LoginResponse;
import org.atinject.api.user.UserAdapter;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.WebSocketClose;
import org.atinject.core.websocket.WebSocketMessage;

@ApplicationScoped
public class AuthenticationWebSocketService extends WebSocketService {

    @Inject AuthenticationService authenticationService;
    
    @Inject AuthenticationDTOFactory authenticationDTOFactory;
    
    @Inject UserAdapter userAdapter;
    
    @WebSocketMessage
    public LoginResponse onLoginRequest(@NonNull LoginRequest request, @NonNull UserSession session){
        UserEntity userEntity = authenticationService.login(session, request.getUsername(), request.getPasswordHash());
        userAdapter.userEntityToUser(userEntity);
        return authenticationDTOFactory.newLoginResponse();
    }
    
    @WebSocketClose
    public void onLogoutRequest(UserSession session){
        authenticationService.logout(session);
    }
    
}
