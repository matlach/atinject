define([],
function(){
	
	function Logger(level) {
		this.level = level;
	};

	Logger.prototype.TRACE_LEVEL = 0;
	Logger.prototype.DEBUG_LEVEL = 1;
	Logger.prototype.INFO_LEVEL = 2;
	Logger.prototype.WARN_LEVEL = 3;
	Logger.prototype.ERROR_LEVEL = 4;

	Logger.prototype.trace = function(string) {
		if (this.level == this.TRACE_LEVEL){
			console.log("TRACE " + string);
		}
	};

	Logger.prototype.debug = function(string) {
		if (this.level <= this.DEBUG_LEVEL){
			console.log("DEBUG " + string);
		}
	};

	Logger.prototype.info = function(string) {
		if (this.level <= this.INFO_LEVEL){
			console.log("INFO " + string);
		}
	};

	Logger.prototype.warn = function(string) {
		if (this.level <= this.WARN_LEVEL){
			console.log("WARN " + string);
		}
	};

	Logger.prototype.error = function(string) {
		if (this.level <= this.ERROR_LEVEL){
			console.log("ERROR " + string);
		}
	};
});