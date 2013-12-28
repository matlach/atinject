define(["atinject/WebSocketResponse"],
function(WebSocketResponse){
    function UpdateUserNameResponse() {
        this._class = "org.atinject.api.user.dto.UpdateUserNameResponse"
    };

    UpdateUserNameResponse.prototype = new WebSocketResponse();

    
    
    return UpdateUserNameResponse;
});
