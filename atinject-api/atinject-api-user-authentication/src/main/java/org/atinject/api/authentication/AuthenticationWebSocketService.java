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
import org.atinject.core.websocket.OnClose;
import org.atinject.core.websocket.OnMessage;

@ApplicationScoped
public class AuthenticationWebSocketService extends WebSocketService {

    @Inject AuthenticationService authenticationService;
    
    @Inject AuthenticationDTOFactory authenticationDTOFactory;
    
    @Inject UserAdapter userAdapter;
    
    @OnMessage
    public LoginResponse onLoginRequest(@NonNull LoginRequest request, @NonNull UserSession session){
        UserEntity userEntity = authenticationService.login(session, request.getUsername(), request.getPassword());
        userAdapter.userEntityToUser(userEntity);
        return authenticationDTOFactory.newLoginResponse();
    }
    
    @OnClose
    public void onLogoutRequest(UserSession session){
        authenticationService.logout(session);
    }
    
}
