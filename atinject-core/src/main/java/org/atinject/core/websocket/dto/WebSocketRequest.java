package org.atinject.core.websocket.dto;

import java.util.UUID;

import org.atinject.core.dto.DTO;

public abstract class WebSocketRequest extends DTO {
    
    private static final long serialVersionUID = 1L;

    private UUID requestId;
    private UUID rendezvous;
    
    public WebSocketRequest(){
        super();
    }

    public UUID getRequestId()
    {
        return requestId;
    }

    public WebSocketRequest setRequestId(UUID requestId)
    {
        this.requestId = requestId;
        return this;
    }

    public UUID getRendezvous() {
        return rendezvous;
    }

    public WebSocketRequest setRendezvous(UUID rendezvous) {
        this.rendezvous = rendezvous;
        return this;
    }
    
}
