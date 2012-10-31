package org.atinject.api.user.dto;

import org.atinject.core.websocket.BaseWebSocketResponse;

public class GetUserResponse extends BaseWebSocketResponse
{

    private User user;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
    
}
