package org.atinject.api.user;

import javax.inject.Inject;

import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.transaction.InfinispanTransactional;
import org.atinject.core.websocket.WebSocketMessage;

@InfinispanTransactional
public class UserWebSocketControllerImpl implements UserWebSocketController {

	@Inject
	private UserService userService;
	
	@Inject
	private UserAdapter userAdapter;
	
	@WebSocketMessage
	public GetUserResponse getUser(GetUserRequest request){
		UserEntity userEntity = userService.getUser("");
	    
		return null;
	}
}
