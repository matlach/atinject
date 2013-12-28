define(["atinject/WebSocketResponse", "atinject/user/User"],
function(WebSocketResponse, User){
    function GetUserResponse() {
        this._class = "org.atinject.api.user.dto.GetUserResponse"
    };

    GetUserResponse.prototype = new WebSocketResponse();

        GetUserResponse.prototype.getUser = function() {
        return this.user;
    };

    GetUserResponse.prototype.setUser = function(user) {
        this.user = user;
        return this;
    };


    
    return GetUserResponse;
});
