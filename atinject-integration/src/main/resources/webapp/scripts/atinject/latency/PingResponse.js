define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function PingResponse() {
        this._class = "org.atinject.core.latency.dto.PingResponse"
    };

    PingResponse.prototype = new WebSocketResponse();

        PingResponse.prototype.getTime = function() {
        return this.time;
    };

    PingResponse.prototype.setTime = function(time) {
        this.time = time;
        return this;
    };


    
    return PingResponse;
});
