define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function LogoutRequest() {
        this._class = "org.atinject.api.authentication.dto.LogoutRequest"
    };

    LogoutRequest.prototype = new WebSocketRequest();

    
    
    return LogoutRequest;
});
