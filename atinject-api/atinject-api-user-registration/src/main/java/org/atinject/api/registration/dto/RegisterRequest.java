package org.atinject.api.registration.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class RegisterRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;
    
    private String newUsername;

    private String newPassword;

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

	public String getNewUsername() {
		return newUsername;
	}

	public RegisterRequest setNewUsername(String newUsername) {
		this.newUsername = newUsername;
		return this;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public RegisterRequest setNewPassword(String newPassword) {
		this.newPassword = newPassword;
		return this;
	}

}
