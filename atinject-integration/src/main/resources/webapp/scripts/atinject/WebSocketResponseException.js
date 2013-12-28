define(["atinject/DTO"],
function(DTO){
    function WebSocketResponseException() {
        this._class = "org.atinject.core.websocket.dto.WebSocketResponseException"
    };

    WebSocketResponseException.prototype = new DTO();

    
    
    return WebSocketResponseException;
});
