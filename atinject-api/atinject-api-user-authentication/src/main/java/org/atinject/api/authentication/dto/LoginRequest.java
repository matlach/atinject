package org.atinject.api.authentication.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class LoginRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }
    
    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
    
    
}
