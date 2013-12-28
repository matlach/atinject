define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function LogoutResponse() {
        this._class = "org.atinject.api.authentication.dto.LogoutResponse"
    };

    LogoutResponse.prototype = new WebSocketResponse();

    
    
    return LogoutResponse;
});
