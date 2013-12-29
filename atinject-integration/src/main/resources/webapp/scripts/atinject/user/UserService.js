define(["atinject/user/UserCache", "atinject/user/UserWebSocketService", "atinject/user/UserFactory"],
function(userCache, userWebSocketService, userFactory){
	
	function UserService(userCache, userWebSocketService, userFactory) {
		this.userCache = userCache;
		this.userWebSocketService = userWebSocketService;
		this.userFactory = userFactory;
	};

	UserService.prototype.getUser = function(userId) {
		// look up in cache first
		var user = this.userCache.getUser(userId);
		if (user != null){
			// found in cache, dispatch
			var event = new CustomEvent("onGetUser_"+userId, {detail:user});
			window.dispatchEvent(event);
		}
		// not in cache, ask server
		var request = userFactory.newGetUserRequest().setUserId(userId);
		this.userWebSocketService.doGetUserRequest(request);
	};

	UserService.prototype.onGetUser = function(request, response) {
		// got response back from server
		if (response.getUser() != null){
			// put object in cache
			this.userCache.putUser(response.getUser().getId(), response.getUser());
		}
		// dispatch
		var event = new CustomEvent("onGetUser_"+request.getUserId(), {detail:user});
		window.dispatchEvent(event);
	};
	
	var userService = new UserService(userCache, userWebSocketService, userFactory);
	window.addEventListener("onGetUserRequestResponse", function(e){userService.onGetUser(e.detail.request, e.detail.response);}, false);

	return userService;
});
