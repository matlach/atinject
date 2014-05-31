package org.atinject.core.websocket.dto;

import org.atinject.core.dto.DTO;

public class WebSocketResponseFault extends DTO {
	
    private static final long serialVersionUID = 1L;

    private String cause;

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
    
}
