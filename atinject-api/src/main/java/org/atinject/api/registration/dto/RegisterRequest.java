package org.atinject.api.registration.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class RegisterRequest extends WebSocketRequest {

	private static final long serialVersionUID = 1L;

	private String username;
	
	private String password;

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
	
	
}
