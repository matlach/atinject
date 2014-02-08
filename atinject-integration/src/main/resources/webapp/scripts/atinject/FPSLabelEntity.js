define(["createjs/easeljs"],
function(easeljs) {
	
	function FPSLabelEntity() {
		this.label = new createjs.Text("-- fps", "bold 14px Arial", "#000");
		this.label.x = 10;
		this.label.y = 20;
	};
	
	FPSLabelEntity.prototype.update = function(){
		label.text = Math.round(createjs.Ticker.getMeasuredFPS()) + " fps";
	};
	
	return new FPSLabelEntity();
	
});
