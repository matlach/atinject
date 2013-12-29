define(["atinject/DTOObjectMapper"],
function(dtoObjectMapper) {
	
	function WebSocketClient(dtoObjectMapper) {
		this.socket;
		this.dtoObjectMapper = dtoObjectMapper;
	};
	
	WebSocketClient.prototype.openWebSocket = function(){
		if (!window.WebSocket) {
		  window.WebSocket = window.MozWebSocket;
		}
		if (window.WebSocket) {
		  this.socket = new WebSocket("ws://localhost:8080/websocket");
		  this.socket.onopen = this.onWebSocketOpened;
		  this.socket.onmessage = this.onWebSocketMessage;
		  this.socket.onclose = this.onWebSocketClosed;
		} else {
		  alert("Your browser does not support Web Socket.");
		}	
	};

	WebSocketClient.prototype.sendWebSocketRequest = function(request) {
		this.sendWebSocketMessage(JSON.stringify(request))
	};
	
	WebSocketClient.prototype.sendWebSocketMessage = function(message) {
	  if (!window.WebSocket) { return; }
	  if (socket.readyState == WebSocket.OPEN) {
		  //var input = document.getElementById("input");
		  //var message = input.value;
	    this.socket.send(message);
	  } else {
	    alert("The socket is not open.");
	  }
	};

	WebSocketClient.prototype.closeWebSocket = function(){
		if (!window.WebSocket) { return; }
		this.socket.close();
	};
	
	WebSocketClient.prototype.onWebSocketOpened = function(event){
		var io = document.getElementById("io");
	    io.value = io.value + "Web Socket opened!";
	};

	WebSocketClient.prototype.onWebSocketMessage = function(event){
		var io = document.getElementById("io");
	    io.value = io.value + "\n" + event.data;
	    this.processWebSocketMessage(event.data);
	};
	
	WebSocketClient.prototype.processWebSocketMessage = function(message) {
		var notification = this.dtoObjectMapper.cast(JSON.parse(message));
		var eventId = "on" + notification.getClassSimpleName();
		var event = new CustomEvent(eventId, {detail:notification});
		window.dispatchEvent(event);
	};

	WebSocketClient.prototype.onWebSocketClosed = function(event){
		var io = document.getElementById('io');
	    io.value = io.value + "Web Socket closed";
	};
	
	return new WebSocketClient();
});