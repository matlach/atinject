define(["createjs/easeljs", "createjs/preloadjs", "createjs/soundjs", "createjs/tweenjs", "atinject/InputService", "atinject/FPSLabelEntity"],
function(easeljs, preloadjs, soundjs, tweenjs, inputService, FPSLabelEntity) {

	function DemoStage(inputService) {
		this.objects = [];
		this.stage = null;
		this.inputService = inputService;
	};
	
	DemoStage.prototype.initialize = function() {
		var canvas = document.getElementById("canvas");
		this.stage = new createjs.Stage(canvas);
		userEntity = new UserEntity(inputService);
		this.stage.addChild(userEntity.getShape());
		this.objects.push(userObject);
		fpsLabelEntity = new FPSLabelEntity();
	    this.stage.addChild(fpsLabel);
	    this.objects.push(fpsLabelEntity);
	};
	
	DemoStage.prototype.update = function(e) {
		this.objects.forEach(function(entry) {
		    entry.update(e.delta/10000);
		});
		this.stage.update();
	};
	
	var demoStage = new DemoStage(inputService);
	
	createjs.Ticker.setFPS(30);
	createjs.Ticker.useRAF = true;
	createjs.Ticker.addEventListener("tick", demoStage.update);
	
	return demoStage;
	
});
