package org.atinject.api.registration.dto;

import org.atinject.api.user.dto.User;
import org.atinject.core.websocket.dto.WebSocketResponse;

public class RegisterAsGuestResponse extends WebSocketResponse {

    private static final long serialVersionUID = 1L;
    
    private User user;

    public RegisterAsGuestResponse(){
        super();
    }

    public User getUser() {
        return user;
    }

    public RegisterAsGuestResponse setUser(User user) {
        this.user = user;
        return this;
    }
    
}
