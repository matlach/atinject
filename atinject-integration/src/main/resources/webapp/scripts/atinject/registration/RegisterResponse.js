define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function RegisterResponse() {
        this._class = "org.atinject.api.registration.dto.RegisterResponse"
    };

    RegisterResponse.prototype = new WebSocketResponse();

    
    
    return RegisterResponse;
});
