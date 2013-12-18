package org.atinject.api.authentication.dto;

import javax.validation.constraints.NotNull;

import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.websocket.dto.WebSocketRequest;

public class LoginRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;

    @NotNull private String username;
    @NotNull private String password;
    
    @NonNull
    public String getUsername() {
        return username;
    }
    
    public LoginRequest setUsername(@NonNull String username) {
        this.username = username;
        return this;
    }
    
    @NonNull
    public String getPassword() {
        return password;
    }
    
    public LoginRequest setPassword(@NonNull String password) {
        this.password = password;
        return this;
    }
    
    
}
