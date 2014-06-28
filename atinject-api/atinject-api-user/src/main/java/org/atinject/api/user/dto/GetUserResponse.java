package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.WebSocketResponse;

public class GetUserResponse extends WebSocketResponse
{
    private static final long serialVersionUID = 1L;

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
