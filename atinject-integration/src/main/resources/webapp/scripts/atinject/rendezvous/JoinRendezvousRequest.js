define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function JoinRendezvousRequest() {
        this._class = "org.atinject.core.rendezvous.dto.JoinRendezvousRequest"
    };

    JoinRendezvousRequest.prototype = new WebSocketRequest();

    
    
    return JoinRendezvousRequest;
});
