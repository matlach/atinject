define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function LoginRequest() {
        this._class = "org.atinject.api.authentication.dto.LoginRequest"
    };

    LoginRequest.prototype = new WebSocketRequest();

        LoginRequest.prototype.getUsername = function() {
        return this.username;
    };

    LoginRequest.prototype.setUsername = function(username) {
        this.username = username;
        return this;
    };

    LoginRequest.prototype.getPassword = function() {
        return this.password;
    };

    LoginRequest.prototype.setPassword = function(password) {
        this.password = password;
        return this;
    };


    
    return LoginRequest;
});
