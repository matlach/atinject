define(["atinject/InputPreferenceService"],
function(inputPreferenceService) {
	
	function InputService(inputPreferenceService) {
		this.inputPreferenceService = inputPreferenceService;
		
		this.leftPressed = false;
		this.leftPressedAt = 0;
		this.leftReleasedAt = 0;
		
		this.rightPressed = false;
		this.rightPressedAt = 0;
		this.rightReleasedAt = 0;
		
		this.upPressed = false;
		this.upPressedAt = 0;
		this.upReleasedAt = 0;
		
		this.downPressed = false;
		this.downPressedAt = 0;
		this.downReleasedAt = 0;
	};

	InputService.prototype.onKeyDown = function(e) {
		if (e.keyCode == inputPreferenceService.up) {
			this.upPressedAt = new Date().getTime();
			this.upPressed = true;
		}
		else if (e.keyCode == inputPreferenceService.down) {
			this.downPressedAt = new Date().getTime();
			this.downPressed = true;
		}
		else if (e.keyCode == inputPreferenceService.left) {
			this.leftPressedAt = new Date().getTime();
			this.leftPressed = true;
		}
		else if (e.keyCode == inputPreferenceService.right) {
			this.rightPressedAt = new Date().getTime();
			this.rightPressed = true;
		}
	};

	InputService.prototype.onKeyUp = function(e) {
		if (e.keyCode == inputPreferenceService.up) {
			this.upPressed = false;
			this.upReleasedAt = new Date().getTime();
		}
		else if (e.keyCode == inputPreferenceService.down) {
			this.downPressed = false;
			this.downReleasedAt = new Date().getTime();
		}
		else if (e.keyCode == inputPreferenceService.left) {
			this.leftPressed = false;
			this.leftReleasedAt = new Date().getTime();
		}
		else if (e.keyCode == inputPreferenceService.right) {
			this.rightPressed = false;
			this.rightReleasedAt = new Date().getTime();
		}
	};

	var inputService = new InputService(inputPreferenceService);
	document.onkeydown = inputService.onKeyDown;
	document.onkeyup = inputService.onKeyUp;
	
	return inputService;
});