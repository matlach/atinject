define(["atinject/DTO"],
function(DTO){
    function User() {
        this._class = "org.atinject.api.user.dto.User"
    };

    User.prototype = new DTO();

        User.prototype.getId = function() {
        return this.id;
    };

    User.prototype.setId = function(id) {
        this.id = id;
        return this;
    };

    User.prototype.getName = function() {
        return this.name;
    };

    User.prototype.setName = function(name) {
        this.name = name;
        return this;
    };


    
    return User;
});
