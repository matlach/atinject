package org.atinject.api.authentication.dto;

import javax.validation.constraints.NotNull;

import org.atinject.core.nullanalysis.NonNull;
import org.atinject.core.websocket.dto.WebSocketRequest;

public class LoginRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;

    @NotNull private String username;
    @NotNull private String passwordHash;
    
    @NonNull
    public String getUsername() {
        return username;
    }
    
    public LoginRequest setUsername(@NonNull String username) {
        this.username = username;
        return this;
    }
    
    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public LoginRequest setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }
    
    
}
