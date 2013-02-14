package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.BaseWebSocketResponse;

public class GetUserResponse extends BaseWebSocketResponse
{

    private User user;

    public User getUser()
    {
        return user;
    }

    public GetUserResponse setUser(User user)
    {
        this.user = user;
        return this;
    }
    
}
