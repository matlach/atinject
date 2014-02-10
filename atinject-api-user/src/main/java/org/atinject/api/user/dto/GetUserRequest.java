package org.atinject.api.user.dto;

import java.util.UUID;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class GetUserRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;
    
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public GetUserRequest setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }
    
    
}
