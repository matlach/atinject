define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function GetTopologyRequest() {
        this._class = "org.atinject.core.topology.dto.GetTopologyRequest"
    };

    GetTopologyRequest.prototype = new WebSocketRequest();

    
    
    return GetTopologyRequest;
});
