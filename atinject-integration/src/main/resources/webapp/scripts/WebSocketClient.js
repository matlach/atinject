define([],
function() {
	
	function WebSocketClient() {
		this.socket;
	};
	
	function openWebSocket(){
		if (!window.WebSocket) {
		  window.WebSocket = window.MozWebSocket;
		}
		if (window.WebSocket) {
		  this.socket = new WebSocket("ws://localhost:8080/websocket");
		  this.socket.onopen = onWebSocketOpened;
		  this.socket.onmessage = onWebSocketMessage;
		  this.socket.onclose = onWebSocketClosed;
		} else {
		  alert("Your browser does not support Web Socket.");
		}	
	};

	function sendWebSocketMessage() {
	  if (!window.WebSocket) { return; }
	  if (socket.readyState == WebSocket.OPEN) {
		  var input = document.getElementById("input");
		  var message = input.value;
	    this.socket.send(message);
	  } else {
	    alert("The socket is not open.");
	  }
	};

	function closeWebSocket(){
		if (!window.WebSocket) { return; }
		this.socket.close();
	};
	
	function onWebSocketOpened(event){
		var io = document.getElementById("io");
	    io.value = io.value + "Web Socket opened!";
	};

	function onWebSocketMessage(event){
		var io = document.getElementById("io");
	    io.value = io.value + "\n" + event.data
	};

	function onWebSocketClosed(event){
		var io = document.getElementById('io');
	    io.value = io.value + "Web Socket closed";
	};
});