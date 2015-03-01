package org.atinject.core.latency.dto;

import org.atinject.core.websocket.dto.WebSocketRequest;

public class PingRequest extends WebSocketRequest {

    private static final long serialVersionUID = 1L;

    private long time;

    public long getTime() {
        return time;
    }

    public PingRequest setTime(long time) {
        this.time = time;
        return this;
    }
    
}
