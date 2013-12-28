define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function GetUserRequest() {
        this._class = "org.atinject.api.user.dto.GetUserRequest"
    };

    GetUserRequest.prototype = new WebSocketRequest();

        GetUserRequest.prototype.getUserId = function() {
        return this.userId;
    };

    GetUserRequest.prototype.setUserId = function(userId) {
        this.userId = userId;
        return this;
    };


    
    return GetUserRequest;
});
