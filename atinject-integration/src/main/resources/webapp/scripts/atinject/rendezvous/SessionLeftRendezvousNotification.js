define(["atinject/WebSocketNotification"],
function(WebSocketNotification){
    function SessionLeftRendezvousNotification() {
        this._class = "org.atinject.core.rendezvous.dto.SessionLeftRendezvousNotification"
    };

    SessionLeftRendezvousNotification.prototype = new WebSocketNotification();

    
    
    return SessionLeftRendezvousNotification;
});
