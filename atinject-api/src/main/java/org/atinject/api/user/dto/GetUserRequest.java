package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class GetUserRequest extends WebSocketRequest
{

    private static final long serialVersionUID = 1L;
    
    private String userId;

    public String getUserId()
    {
        return userId;
    }

    public GetUserRequest setUserId(String userId)
    {
        this.userId = userId;
        return this;
    }
    
    
}
