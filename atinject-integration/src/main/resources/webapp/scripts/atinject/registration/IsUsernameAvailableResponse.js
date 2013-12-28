define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function IsUsernameAvailableResponse() {
        this._class = "org.atinject.api.registration.dto.IsUsernameAvailableResponse"
    };

    IsUsernameAvailableResponse.prototype = new WebSocketResponse();

        IsUsernameAvailableResponse.prototype.getAvailable = function() {
        return this.available;
    };

    IsUsernameAvailableResponse.prototype.setAvailable = function(available) {
        this.available = available;
        return this;
    };


    
    return IsUsernameAvailableResponse;
});
