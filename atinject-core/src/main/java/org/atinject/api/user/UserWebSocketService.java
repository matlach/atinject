package org.atinject.api.user;

import javax.inject.Inject;

import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.WebSocketMessage;

public class UserWebSocketService extends WebSocketService {

	@Inject
	private UserService userService;
	
	@Inject
	private UserAdapter userAdapter;
	
	@WebSocketMessage
	public GetUserResponse getUser(GetUserRequest request){
		UserEntity userEntity = userService.getUser(request.getUserId());
	    User user = userAdapter.userEntityToUser(userEntity);
	    GetUserResponse response = new GetUserResponse();
	    response.setUser(user);
		return response;
	}
	
}
