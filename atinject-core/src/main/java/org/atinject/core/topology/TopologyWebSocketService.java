package org.atinject.core.topology;

import javax.inject.Inject;

import org.atinject.core.session.Session;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.topology.dto.GetTopologyRequest;
import org.atinject.core.topology.dto.GetTopologyResponse;

@WebSocketService
public class TopologyWebSocketService {

    @Inject private TopologyService topologyService;
    
    public GetTopologyResponse getUrl(Session session, GetTopologyRequest request){
        return new GetTopologyResponse()
            .setUrls(topologyService.getAllUrl());
    }
    
}
