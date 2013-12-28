define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function LoginResponse() {
        this._class = "org.atinject.api.authentication.dto.LoginResponse"
    };

    LoginResponse.prototype = new WebSocketResponse();

    
    
    return LoginResponse;
});
