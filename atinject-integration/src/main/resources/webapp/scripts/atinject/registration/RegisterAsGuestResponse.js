define(["atinject/WebSocketResponse", "atinject/user/User"],
function(WebSocketResponse, User){
    function RegisterAsGuestResponse() {
        this._class = "org.atinject.api.registration.dto.RegisterAsGuestResponse"
    };

    RegisterAsGuestResponse.prototype = new WebSocketResponse();

        RegisterAsGuestResponse.prototype.getUser = function() {
        return this.user;
    };

    RegisterAsGuestResponse.prototype.setUser = function(user) {
        this.user = user;
        return this;
    };


    
    return RegisterAsGuestResponse;
});
