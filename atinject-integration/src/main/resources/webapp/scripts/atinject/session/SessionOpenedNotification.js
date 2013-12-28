define(["atinject/WebSocketNotification"],
function(WebSocketNotification){
    function SessionOpenedNotification() {
        this._class = "org.atinject.core.session.dto.SessionOpenedNotification"
    };

    SessionOpenedNotification.prototype = new WebSocketNotification();

        SessionOpenedNotification.prototype.getSessionId = function() {
        return this.sessionId;
    };

    SessionOpenedNotification.prototype.setSessionId = function(sessionId) {
        this.sessionId = sessionId;
        return this;
    };


    
    return SessionOpenedNotification;
});
