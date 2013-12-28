define(["UserCache", "UserWebSocketService", "GetUserRequest", "GetUserResponse", "User"],
function(UserCache, UserWebSocketService, GetUserRequest, GetUserResponse, User){
	
	function UserService(UserCache, UserWebSocketService, GetUserRequest, GetUserResponse, User) {
		this.userCache = UserCache;
		this.userWebSocketService = UserWebSocketService;
		this.GetUserRequest = GetUserRequest;
		this.GetUserResponse = GetUserResponse;
		this.User = User;
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
		var request = new GetUserRequest().setUserId(userId);
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
	
	var userService = new UserService(UserCache, UserWebSocketService, GetUserRequest, GetUserResponse, User);
	window.addEventListener("onGetUserRequestResponse", function(e){userService.onGetUser(e.detail.request, e.detail.response);}, false);

	return userService;
});
