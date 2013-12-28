define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function GetTopologyResponse() {
        this._class = "org.atinject.core.topology.dto.GetTopologyResponse"
    };

    GetTopologyResponse.prototype = new WebSocketResponse();

        GetTopologyResponse.prototype.getUrls = function() {
        return this.urls;
    };

    GetTopologyResponse.prototype.setUrls = function(urls) {
        this.urls = urls;
        return this;
    };


    
    return GetTopologyResponse;
});
