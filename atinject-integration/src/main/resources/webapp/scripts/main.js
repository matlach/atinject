require.config({

});

require(["createjs/easeljs", "createjs/preloadjs", "createjs/soundjs", "createjs/tweenjs"],
function(easeljs, preloadjs, soundjs, tweenjs) {
	initCanvas();
});
require(["atinject/CLASS_ALIAS"], function(atinject) {
	//alert("atinject loaded");
});