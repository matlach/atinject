package org.atinject.api.websocket.server;

import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.atinject.api.usersession.UserSession;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.session.SessionService;
import org.atinject.core.websocket.dto.WebSocketRequest;
import org.atinject.core.websocket.dto.WebSocketResponse;

@Alternative @Specializes
@ApplicationScoped
public class WebSocketServerHandler extends org.atinject.core.websocket.server.WebSocketServerHandler {

	@Inject
    private SessionService sessionService;
	
	@Inject
    private AsynchronousService asynchronousService;
	
	@Inject
    private UserRequestDistributedExecutor distributedExecutor;
	
    // this class distinguish the public and authoritive web socket server handler
	@Override
    public WebSocketResponse performRequest(String sessionId, WebSocketRequest request){
        // build task
        WebSocketMessageTask task = new WebSocketMessageTask()
            .setSessionId(sessionId)
            .setRequest(request);
        
        // should we use a future here to wait for response ?
        // response could be sent asynchronously instead ?
        // should we use completion service ?
        
        UserSession session = (UserSession) sessionService.getSession(sessionId);
        
        Future<WebSocketResponse> future;
        if (session.getUserId() == null){
            if (request.getRendezvous() == null){
                // perform locally through asynchronous service
                // no gain of submitting request on any member of the cluster ?
                // what if we do not use netty's asynchronous handler ?
                future = asynchronousService.submit(task);
            }
            else {
                // submit task to distributed executor with given rendezvous
                future = distributedExecutor.submit(task, request.getRendezvous());
            }
        }
        else{
            if (request.getRendezvous() == null){
                // submit task to distributed executor using user id as rendezvous
                future = distributedExecutor.submit(task, session.getUserId());
            }
            else {
                // submit task to distributed executor with given rendezvous
                future = distributedExecutor.submit(task, request.getRendezvous());
            }
        }
        
        // wait for response to come back
        WebSocketResponse response = getWebSocketResponseFromFuture(future);
        
        // bind the original request id back in the response
        response.setRequestId(request.getRequestId());
        
        return response;
    }
}
