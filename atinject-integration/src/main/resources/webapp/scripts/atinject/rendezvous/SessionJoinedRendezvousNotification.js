define(["atinject/WebSocketNotification"],
function(WebSocketNotification){
    function SessionJoinedRendezvousNotification() {
        this._class = "org.atinject.core.rendezvous.dto.SessionJoinedRendezvousNotification"
    };

    SessionJoinedRendezvousNotification.prototype = new WebSocketNotification();

    
    
    return SessionJoinedRendezvousNotification;
});
