package org.atinject.core.latency;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.latency.dto.PingRequest;
import org.atinject.core.latency.dto.PingResponse;
import org.atinject.core.tiers.WebSocketService;

@ApplicationScoped
public class LatencyWebSocketService extends WebSocketService {

    @Inject LatencyService latencyService;
    
    @Inject LatencyDTOFactory factory;
    
    public PingResponse onPing(PingRequest request){
        return factory.newPingResponse().setTime(latencyService.getTime());
    }
}
