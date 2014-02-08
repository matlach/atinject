define(["atinject/Logger"],
function(Logger) {
	function LoggerFactory(Logger Logger) {
		this.Logger = Logger;
	};

	LoggerFactory.prototype.getLogger = function(name) {
		// TODO get logger configuration
		return new Logger(Logger.INFO_LEVEL);
	};

	return new LoggerFactory();
});
