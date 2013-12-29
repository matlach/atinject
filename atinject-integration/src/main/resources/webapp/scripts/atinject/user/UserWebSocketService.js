define(["atinject/WebSocketClient"],
function(webSocketClient){
	function UserWebSocketService(webSocketClient){
		 this.requests = new Object();
		 this.webSocketClient = webSocketClient
	};

	UserWebSocketService.prototype.doGetUserRequest = function(request){
		var requestId = randomUUID();
		request.setRequestId(requestId);
		this.requests[requestId] = request;
		this.webSocketClient.sendWebSocketRequest(request);
	};
	 
	UserWebSocketService.prototype.onGetUserResponse = function(response){
	    var request = this.requests[response.getRequestId()];
	    delete this.requests[response.getRequestId()];
	    var event = new CustomEvent("onGetUserRequestResponse", {"detail":{"request":request, "response":response}});
		window.dispatchEvent(event);
	};
	var userWebSocketService = new UserWebSocketService(webSocketClient);
	window.addEventListener("onGetUserResponse", function(e){userWebSocketService.onGetUserResponse(e.detail);}, false);
	
	return userWebSocketService;
});
