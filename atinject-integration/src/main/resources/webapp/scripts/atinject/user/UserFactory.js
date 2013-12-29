define(["atinject/user/GetUserRequest", "atinject/user/GetUserResponse", "atinject/user/User"],
function(GetUserRequest, GetUserResponse, User) {
	
	function UserFactory(GetUserRequest, GetUserResponse, User) {
		this.GetUserRequest = GetUserRequest;
		this.GetUserResponse = GetUserResponse;
		this.User = User;
	};
	
	UserFactory.prototype.newGetUserRequest = function() {
		return new GetUserRequest();
	};
	
	UserFactory.prototype.newGetUserResponse = function() {
		return new GetUserResponse();
	}
	
	UserFactory.prototype.newUser = function() {
		return new User();
	}
	
	return new UserFactory(GetUserRequest, GetUserResponse, User);
});