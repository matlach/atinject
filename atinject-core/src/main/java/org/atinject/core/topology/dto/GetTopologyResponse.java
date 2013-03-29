package org.atinject.core.topology.dto;

import java.util.List;

import org.atinject.core.websocket.dto.WebSocketResponse;

public class GetTopologyResponse extends WebSocketResponse {

    private static final long serialVersionUID = 1L;

    private List<String> urls;
    
    public GetTopologyResponse(){
        super();
    }

    public List<String> getUrls() {
        return urls;
    }

    public GetTopologyResponse setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }
    
}
