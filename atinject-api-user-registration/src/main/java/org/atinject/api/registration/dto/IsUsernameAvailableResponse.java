package org.atinject.api.registration.dto;

import org.atinject.core.websocket.dto.WebSocketResponse;

public class IsUsernameAvailableResponse extends WebSocketResponse {

    private static final long serialVersionUID = 1L;

    private boolean available;
    
    public IsUsernameAvailableResponse(){
        super();
    }

    public boolean isAvailable() {
        return available;
    }

    public IsUsernameAvailableResponse setAvailable(boolean available) {
        this.available = available;
        return this;
    }
    
}
