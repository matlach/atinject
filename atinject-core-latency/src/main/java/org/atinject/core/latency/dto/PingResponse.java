package org.atinject.core.latency.dto;

import org.atinject.core.websocket.dto.WebSocketResponse;

public class PingResponse extends WebSocketResponse {

    private static final long serialVersionUID = 1L;

    private long time;

    public long getTime() {
        return time;
    }

    public PingResponse setTime(long time) {
        this.time = time;
        return this;
    }    
}
