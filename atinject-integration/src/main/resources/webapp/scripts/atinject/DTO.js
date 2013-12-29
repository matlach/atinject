define([],
function(){
    function DTO() {
    };
    
    DTO.prototype.getClass = function() {
        return this._class;
    };
    
    DTO.prototype.getClassSimpleName = function() {
        this.getClass().split(".").slice(-1)[0]
    };
    
    return DTO;
});
