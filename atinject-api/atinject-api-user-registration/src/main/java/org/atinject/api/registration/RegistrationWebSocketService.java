package org.atinject.api.registration;

import javax.inject.Inject;

import org.atinject.api.registration.dto.RegisterAsGuestRequest;
import org.atinject.api.registration.dto.RegisterAsGuestResponse;
import org.atinject.api.registration.dto.RegisterRequest;
import org.atinject.api.registration.dto.RegisterResponse;
import org.atinject.api.user.UserAdapter;
import org.atinject.api.user.dto.User;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.OnMessage;

@WebSocketService
public class RegistrationWebSocketService {

    @Inject
    private RegistrationService registrationService;
    
    @Inject
    private UserAdapter userAdapter;

    @OnMessage
    public RegisterAsGuestResponse onRegisterAsGuest(RegisterAsGuestRequest request, UserSession session) {
        RegistratedUser registratedUser = registrationService.registerAsGuest();
        User user = userAdapter.userEntityToUser(registratedUser.getUser());
        return new RegisterAsGuestResponse().setUser(user);
    }

    @OnMessage
    public RegisterResponse onRegister(RegisterRequest request, UserSession session) {
        registrationService.registerFromGuest(
        		request.getUsername(), request.getPassword(),
        		request.getNewUsername(), request.getNewPassword());

        return new RegisterResponse();
    }
}
