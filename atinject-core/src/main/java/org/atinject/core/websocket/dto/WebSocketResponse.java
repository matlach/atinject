package org.atinject.core.websocket.dto;

import java.util.UUID;

import org.atinject.core.dto.DTO;

public abstract class WebSocketResponse extends DTO {
	
    private static final long serialVersionUID = 1L;

    private UUID requestId;
    
    private WebSocketResponseFault fault;
    
    public UUID getRequestId() {
        return requestId;
    }

    public WebSocketResponse setRequestId(UUID requestId) {
        this.requestId = requestId;
        return this;
    }

	public WebSocketResponseFault getFault() {
		return fault;
	}

	public void setFault(WebSocketResponseFault fault) {
		this.fault = fault;
	}

}
