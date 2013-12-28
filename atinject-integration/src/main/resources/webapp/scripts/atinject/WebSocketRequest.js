define(["atinject/DTO"],
function(DTO){
    function WebSocketRequest() {
        this._class = "org.atinject.core.websocket.dto.WebSocketRequest"
    };

    WebSocketRequest.prototype = new DTO();

        WebSocketRequest.prototype.getRequestId = function() {
        return this.requestId;
    };

    WebSocketRequest.prototype.setRequestId = function(requestId) {
        this.requestId = requestId;
        return this;
    };

    WebSocketRequest.prototype.getRendezvous = function() {
        return this.rendezvous;
    };

    WebSocketRequest.prototype.setRendezvous = function(rendezvous) {
        this.rendezvous = rendezvous;
        return this;
    };


    
    return WebSocketRequest;
});
