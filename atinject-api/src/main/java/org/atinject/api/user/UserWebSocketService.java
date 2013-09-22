package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.UpdateUserNameRequest;
import org.atinject.api.user.dto.UpdateUserNameResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.WebSocketMessage;

@ApplicationScoped
public class UserWebSocketService extends WebSocketService {

	@Inject
	UserService userService;
	
	@Inject
	UserAdapter userAdapter;
	
	@WebSocketMessage
	public GetUserResponse getUser(GetUserRequest request){
		UserEntity userEntity = userService.getUser(request.getUserId());
	    User user = userAdapter.userEntityToUser(userEntity);
	    GetUserResponse response = new GetUserResponse().setUser(user);
		return response;
	}
	
	@WebSocketMessage
	public UpdateUserNameResponse onUpdateUserName(UpdateUserNameRequest request, UserSession session){
		userService.updateUserName(session.getUserId(), request.getName());
		
		return new UpdateUserNameResponse();
	}
	
}
