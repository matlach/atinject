package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.authorization.RequiresRoles;
import org.atinject.api.registration.dto.RegisterAsGuestRequest;
import org.atinject.api.registration.dto.RegisterAsGuestResponse;
import org.atinject.api.registration.dto.RegisterRequest;
import org.atinject.api.registration.dto.RegisterResponse;
import org.atinject.api.role.enumeration.SimpleRoles;
import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.WebSocketMessage;

@ApplicationScoped
public class RegistrationWebSocketService extends WebSocketService {

	@Inject RegistrationService registrationService;
	@Inject UserAdapter userAdapter;
	
	@WebSocketMessage
	public RegisterAsGuestResponse onRegisterAsGuest(RegisterAsGuestRequest request, UserSession session){
		UserEntity userEntity = registrationService.registerAsGuest();
		User user = userAdapter.userEntityToUser(userEntity);
		return new RegisterAsGuestResponse().setUser(user);
	}
	
	@RequiresRoles(SimpleRoles.GUEST)
	@WebSocketMessage
	public RegisterResponse onRegister(RegisterRequest request, UserSession session) {
		registrationService.register(session.getUserId(), request.getUsername(), request.getPassword());
		
		return new RegisterResponse();
	}
}
