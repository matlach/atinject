package org.atinject.core.websocket.dto;

import org.atinject.core.dto.DTO;

public abstract class BaseWebSocketResponse extends DTO
{
    private static final long serialVersionUID = 1L;

    private String requestId;
    
    public BaseWebSocketResponse(){
        super();
    }

    public String getRequestId()
    {
        return requestId;
    }

    public BaseWebSocketResponse setRequestId(String requestId)
    {
        this.requestId = requestId;
        return this;
    }
}
