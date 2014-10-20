package org.atinject.api.user;

import javax.inject.Inject;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.UpdateUserNameRequest;
import org.atinject.api.user.dto.UpdateUserNameResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.websocket.OnMessage;

@WebSocketService
public class UserWebSocketService {

    @Inject
    UserService userService;

    @Inject
    UserAdapter userAdapter;

    @Inject
    UserDTOFactory userDTOFactory;

    @OnMessage
    public GetUserResponse getUser(GetUserRequest request) {
        UserEntity userEntity = userService.getUser(request.getUserId());
        User user = userAdapter.userEntityToUser(userEntity);
        return userDTOFactory.newGetUserResponse().setUser(user);
    }

    @OnMessage
    public UpdateUserNameResponse onUpdateUserName(UpdateUserNameRequest request, UserSession session) {
        userService.updateUserName(session.getUserId(), request.getName());

        return new UpdateUserNameResponse();
    }

}
