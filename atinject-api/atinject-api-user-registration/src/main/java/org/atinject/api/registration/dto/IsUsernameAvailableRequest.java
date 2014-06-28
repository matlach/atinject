package org.atinject.api.registration.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class IsUsernameAvailableRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;
    
    private String username;
    
    public IsUsernameAvailableRequest(){
        super();
    }

    public String getUsername() {
        return username;
    }

    public IsUsernameAvailableRequest setUsername(String username) {
        this.username = username;
        return this;
    }
    
}
