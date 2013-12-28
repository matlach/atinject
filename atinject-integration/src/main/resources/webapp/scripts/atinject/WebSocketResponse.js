define(["atinject/DTO", "atinject/WebSocketResponseException"],
function(DTO, WebSocketResponseException){
    function WebSocketResponse() {
        this._class = "org.atinject.core.websocket.dto.WebSocketResponse"
    };

    WebSocketResponse.prototype = new DTO();

        WebSocketResponse.prototype.getRequestId = function() {
        return this.requestId;
    };

    WebSocketResponse.prototype.setRequestId = function(requestId) {
        this.requestId = requestId;
        return this;
    };

    WebSocketResponse.prototype.getException = function() {
        return this.exception;
    };

    WebSocketResponse.prototype.setException = function(exception) {
        this.exception = exception;
        return this;
    };


    
    return WebSocketResponse;
});
