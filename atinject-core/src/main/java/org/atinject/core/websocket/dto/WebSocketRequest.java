package org.atinject.core.websocket.dto;

import org.atinject.core.dto.DTO;

public abstract class WebSocketRequest extends DTO
{
    private static final long serialVersionUID = 1L;

    private String requestId;
    
    public WebSocketRequest(){
        super();
    }

    public String getRequestId()
    {
        return requestId;
    }

    public WebSocketRequest setRequestId(String requestId)
    {
        this.requestId = requestId;
        return this;
    }
    
}
