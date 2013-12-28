define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function IsUsernameAvailableRequest() {
        this._class = "org.atinject.api.registration.dto.IsUsernameAvailableRequest"
    };

    IsUsernameAvailableRequest.prototype = new WebSocketRequest();

        IsUsernameAvailableRequest.prototype.getUsername = function() {
        return this.username;
    };

    IsUsernameAvailableRequest.prototype.setUsername = function(username) {
        this.username = username;
        return this;
    };


    
    return IsUsernameAvailableRequest;
});
