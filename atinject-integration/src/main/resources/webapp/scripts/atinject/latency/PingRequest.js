define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function PingRequest() {
        this._class = "org.atinject.core.latency.dto.PingRequest"
    };

    PingRequest.prototype = new WebSocketRequest();

        PingRequest.prototype.getTime = function() {
        return this.time;
    };

    PingRequest.prototype.setTime = function(time) {
        this.time = time;
        return this;
    };


    
    return PingRequest;
});
