define([],
function() {
	
	function InputPreferenceService() {
		this.up = this.DEFAULT_UP;
		this.down = this.DEFAULT_DOWN;
		this.left = this.DEFAULT_LEFT;
		this.right = this.DEFAULT_RIGHT;
	};

	InputPreferenceService.prototype.DEFAULT_UP = 87;
	InputPreferenceService.prototype.DEFAULT_DOWN = 83;
	InputPreferenceService.prototype.DEFAULT_LEFT = 65;
	InputPreferenceService.prototype.DEFAULT_RIGHT = 68;

	return new InputPreferenceService();
});

