package org.atinject.core.websocket.dto;

import org.atinject.core.dto.BaseDTO;

public abstract class BaseWebSocketRequest extends BaseDTO
{
    private static final long serialVersionUID = 1L;

    private String requestId;
    
    public BaseWebSocketRequest(){
        super();
    }

    public String getRequestId()
    {
        return requestId;
    }

    public BaseWebSocketRequest setRequestId(String requestId)
    {
        this.requestId = requestId;
        return this;
    }
    
}
