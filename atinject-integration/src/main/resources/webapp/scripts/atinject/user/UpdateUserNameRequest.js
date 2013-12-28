define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function UpdateUserNameRequest() {
        this._class = "org.atinject.api.user.dto.UpdateUserNameRequest"
    };

    UpdateUserNameRequest.prototype = new WebSocketRequest();

        UpdateUserNameRequest.prototype.getName = function() {
        return this.name;
    };

    UpdateUserNameRequest.prototype.setName = function(name) {
        this.name = name;
        return this;
    };


    
    return UpdateUserNameRequest;
});
