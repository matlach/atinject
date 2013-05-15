package org.atinject.core.topology;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.session.Session;
import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.topology.dto.GetTopologyRequest;
import org.atinject.core.topology.dto.GetTopologyResponse;

@ApplicationScoped
public class TopologyWebSocketService extends WebSocketService {

    @Inject private TopologyService topologyService;
    
    public GetTopologyResponse getUrl(Session session, GetTopologyRequest request){
        return new GetTopologyResponse()
            .setUrls(topologyService.getAllUrl());
    }
    
}
