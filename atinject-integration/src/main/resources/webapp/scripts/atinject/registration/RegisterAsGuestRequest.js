define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function RegisterAsGuestRequest() {
        this._class = "org.atinject.api.registration.dto.RegisterAsGuestRequest"
    };

    RegisterAsGuestRequest.prototype = new WebSocketRequest();

    
    
    return RegisterAsGuestRequest;
});
