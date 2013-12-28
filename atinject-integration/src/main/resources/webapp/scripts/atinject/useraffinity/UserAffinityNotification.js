define(["atinject/WebSocketNotification"],
function(WebSocketNotification){
    function UserAffinityNotification() {
        this._class = "org.atinject.api.useraffinity.dto.UserAffinityNotification"
    };

    UserAffinityNotification.prototype = new WebSocketNotification();

        UserAffinityNotification.prototype.getUrl = function() {
        return this.url;
    };

    UserAffinityNotification.prototype.setUrl = function(url) {
        this.url = url;
        return this;
    };


    
    return UserAffinityNotification;
});
