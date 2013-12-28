define(["UserService"],
function(UserService){

	function UserView() {
		this.listeners = new Object();
		this.gettingUser = false;
		this.userService = UserService;
	};

	UserView.prototype.getUser = function(userId) {
		if (this.gettingUser){
			return;
		}
		this.gettingUser = true;
		var listener = function(e){userView.onGetUser(e.detail);}
		var listenerId = "onGetUser_" + userId;
		this.listeners[listenerId] = listener;
		window.addEventListener(listenerId, listener, false);
		this.userService.getUser(userId);
	};

	UserView.prototype.onGetUser = function(user) {
		var listenerId = "onGetUser_"+user+getUserId();
		var listener = this.listeners[listenerId];
		window.removeEventListener(listenerId, listener, false);
		alert(user.getUserId());
		this.gettingUser = false;
	};
	var userView = new UserView();
	
	return userView;
});
