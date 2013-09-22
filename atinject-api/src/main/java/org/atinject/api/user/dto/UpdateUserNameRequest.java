package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class UpdateUserNameRequest extends WebSocketRequest {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public String getName() {
		return name;
	}

	public UpdateUserNameRequest setName(String name) {
		this.name = name;
		return this;
	}
	
	
}
