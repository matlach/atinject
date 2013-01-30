package org.atinject.api.user.dto;

import org.atinject.core.websocket.BaseWebSocketRequest;

public class GetUserRequest extends BaseWebSocketRequest
{

    private static final long serialVersionUID = 1L;
    
    private String uuid;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
    
    
}
