package org.atinject.api.user;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.user.adapter.UserAdapter;
import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.dto.UserAffinityNotification;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserLoggedIn;
import org.atinject.core.distexec.UserKey;
import org.atinject.core.distexec.UserTopologyService;
import org.atinject.core.notification.NotificationService;
import org.atinject.core.transaction.InfinispanTransactional;
import org.atinject.core.websocket.WebSocketMessage;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@InfinispanTransactional
public class UserWebSocketControllerImpl implements UserWebSocketController {

	@Inject
	private UserService userService;
	
	@Inject
	private UserAdapter userAdapter;
	
	@Inject
	private NotificationService notificationService;
	
	@Inject
    private UserTopologyService userTopologyService;
	
	@WebSocketMessage
	public GetUserResponse getUser(GetUserRequest request){
		UserEntity userEntity = userService.getUser("");
	    User user = userAdapter.userEntityToUser(userEntity);
	    GetUserResponse response = new GetUserResponse();
	    response.setUser(user);
		return response;
	}
	
	public void onUserLoggedIn(@Observes UserLoggedIn event){
	    // user logged in locally i.e. on this websocket
	    if (event.isOriginLocal()){
	        // get user affinity
	        UserKey userKey = new UserKey();
	        userKey.setId(event.getUser().getId());
	        TopologyAwareAddress topologyAwareAddress = userTopologyService.getUserKeyPrimaryLocation(userKey);
	        
	        // send notification with user affinity information
	        UserAffinityNotification userAffinityNotification = new UserAffinityNotification();
	        userAffinityNotification.setUrl(topologyAwareAddress.getMachineId());
	        notificationService.sendNotification(event.getSession(), userAffinityNotification);
	    }
	}
	
	// TODO on topology changed, recompute for each local client (i.e. session with machineId = localAddress.machineId) the best affinity
	
}
