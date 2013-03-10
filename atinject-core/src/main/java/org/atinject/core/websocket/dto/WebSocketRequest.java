package org.atinject.core.websocket.dto;

import org.atinject.core.dto.DTO;

public abstract class WebSocketRequest extends DTO {
    
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String rendezvous;
    
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

    public String getRendezvous() {
        return rendezvous;
    }

    public WebSocketRequest setRendezvous(String rendezvous) {
        this.rendezvous = rendezvous;
        return this;
    }
    
}
