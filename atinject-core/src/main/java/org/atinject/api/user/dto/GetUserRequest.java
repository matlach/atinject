package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.BaseWebSocketRequest;

public class GetUserRequest extends BaseWebSocketRequest
{

    private static final long serialVersionUID = 1L;
    
    private String uuid;

    public String getUuid()
    {
        return uuid;
    }

    public GetUserRequest setUuid(String uuid)
    {
        this.uuid = uuid;
        return this;
    }
    
    
}
