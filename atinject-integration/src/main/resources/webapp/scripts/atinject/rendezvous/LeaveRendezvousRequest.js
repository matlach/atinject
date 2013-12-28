define(["atinject/WebSocketRequest"],
function(WebSocketRequest){
    function LeaveRendezvousRequest() {
        this._class = "org.atinject.core.rendezvous.dto.LeaveRendezvousRequest"
    };

    LeaveRendezvousRequest.prototype = new WebSocketRequest();

    
    
    return LeaveRendezvousRequest;
});
