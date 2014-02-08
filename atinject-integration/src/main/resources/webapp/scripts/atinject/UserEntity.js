define(["createjs/easeljs"],
function(easeljs) {
	
	function UserEntity(inputService) {
		this.inputService = inputService;

		this.shape = new createjs.Shape();
		this.shape.graphics.f("red").r(50, 50, 100, 100);
		this.speed = 50;
	};

	UserObject.prototype.getShape = function () {
		return this.shape;
	};

	UserObject.prototype.update = function(delta) {
		dirX = 0;
		dirY = 0;
		if(this.inputService.leftPressed) {dirX = -1;}
		if(this.inputService.rightPressed) {dirX = 1;}
		if(this.inputService.upPressed) {dirY = -1;}
		if(this.inputService.downPressed) {dirY = 1;}
		
		this.shape.x = this.shape.x + dirX * (delta * this.speed);
		this.shape.y = this.shape.y + dirY * (delta * this.speed);
	};
	
	return UserEntity;
});