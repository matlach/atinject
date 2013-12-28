define(["atinject/DTO"],
function(DTO){
    function SystemProperty() {
        this._class = "org.atinject.api.systemproperty.dto.SystemProperty"
    };

    SystemProperty.prototype = new DTO();

        SystemProperty.prototype.getKey = function() {
        return this.key;
    };

    SystemProperty.prototype.setKey = function(key) {
        this.key = key;
        return this;
    };

    SystemProperty.prototype.getValue = function() {
        return this.value;
    };

    SystemProperty.prototype.setValue = function(value) {
        this.value = value;
        return this;
    };


    
    return SystemProperty;
});
