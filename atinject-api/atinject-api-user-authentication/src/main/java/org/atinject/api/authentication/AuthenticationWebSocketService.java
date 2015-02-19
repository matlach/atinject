package org.atinject.api.authentication;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.authentication.dto.LoginRequest;
import org.atinject.api.authentication.dto.LoginResponse;
import org.atinject.api.user.UserAdapter;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.OnClose;
import org.atinject.core.websocket.OnMessage;

@WebSocketService
public class AuthenticationWebSocketService {

    @Inject AuthenticationService authenticationService;
    
    @Inject AuthenticationDTOFactory authenticationDTOFactory;
    
    @Inject UserAdapter userAdapter;
    
    @OnMessage
    public LoginResponse onLoginRequest(@NotNull LoginRequest request, @NotNull UserSession session){
        UserEntity userEntity = authenticationService.login(session, request.getUsername(), request.getPassword());
        userAdapter.userEntityToUser(userEntity);
        return authenticationDTOFactory.newLoginResponse();
    }
    
    @OnClose
    public void onLogoutRequest(UserSession session){
        authenticationService.logout(session);
    }
    
}
