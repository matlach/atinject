package org.atinject.core.latency;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.dto.DTOFactory;
import org.atinject.core.latency.dto.PingRequest;
import org.atinject.core.latency.dto.PingResponse;

@ApplicationScoped
public class LatencyDTOFactory extends DTOFactory {

    public PingRequest newPingRequest() {
        return new PingRequest();
    }
    
    public PingResponse newPingResponse() {
        return new PingResponse();
    }
}
