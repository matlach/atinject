define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function RegisterRequest() {
        this._class = "org.atinject.api.registration.dto.RegisterRequest"
    };

    RegisterRequest.prototype = new WebSocketRequest();

        RegisterRequest.prototype.getUsername = function() {
        return this.username;
    };

    RegisterRequest.prototype.setUsername = function(username) {
        this.username = username;
        return this;
    };

    RegisterRequest.prototype.getPassword = function() {
        return this.password;
    };

    RegisterRequest.prototype.setPassword = function(password) {
        this.password = password;
        return this;
    };


    
    return RegisterRequest;
});
