define(["atinject/WebSocketRequest", "atinject/WebSocketResponseException", "atinject/DTO", "atinject/WebSocketNotification", "atinject/WebSocketResponse", "atinject/authentication/LogoutRequest", "atinject/authentication/LoginRequest", "atinject/authentication/LogoutResponse", "atinject/authentication/LoginResponse", "atinject/latency/PingResponse", "atinject/latency/PingRequest", "atinject/registration/RegisterAsGuestRequest", "atinject/registration/RegisterAsGuestResponse", "atinject/registration/RegisterRequest", "atinject/registration/IsUsernameAvailableRequest", "atinject/registration/IsUsernameAvailableResponse", "atinject/registration/RegisterResponse", "atinject/rendezvous/SessionJoinedRendezvousNotification", "atinject/rendezvous/SessionLeftRendezvousNotification", "atinject/rendezvous/JoinRendezvousRequest", "atinject/rendezvous/LeaveRendezvousRequest", "atinject/session/SessionOpenedNotification", "atinject/systemproperty/SystemProperty", "atinject/topology/GetTopologyResponse", "atinject/topology/GetTopologyRequest", "atinject/user/UpdateUserNameResponse", "atinject/user/UpdateUserNameRequest", "atinject/user/GetUserRequest", "atinject/user/GetUserResponse", "atinject/user/User", "atinject/useraffinity/UserAffinityNotification"],
function(WebSocketRequest, WebSocketResponseException, DTO, WebSocketNotification, WebSocketResponse, LogoutRequest, LoginRequest, LogoutResponse, LoginResponse, PingResponse, PingRequest, RegisterAsGuestRequest, RegisterAsGuestResponse, RegisterRequest, IsUsernameAvailableRequest, IsUsernameAvailableResponse, RegisterResponse, SessionJoinedRendezvousNotification, SessionLeftRendezvousNotification, JoinRendezvousRequest, LeaveRendezvousRequest, SessionOpenedNotification, SystemProperty, GetTopologyResponse, GetTopologyRequest, UpdateUserNameResponse, UpdateUserNameRequest, GetUserRequest, GetUserResponse, User, UserAffinityNotification){
    var classAlias = new Array();
        classAlias["org.atinject.core.websocket.dto.WebSocketRequest"] = WebSocketRequest;
    classAlias["org.atinject.core.websocket.dto.WebSocketResponseException"] = WebSocketResponseException;
    classAlias["org.atinject.core.dto.DTO"] = DTO;
    classAlias["org.atinject.core.websocket.dto.WebSocketNotification"] = WebSocketNotification;
    classAlias["org.atinject.core.websocket.dto.WebSocketResponse"] = WebSocketResponse;
    classAlias["org.atinject.api.authentication.dto.LogoutRequest"] = LogoutRequest;
    classAlias["org.atinject.api.authentication.dto.LoginRequest"] = LoginRequest;
    classAlias["org.atinject.api.authentication.dto.LogoutResponse"] = LogoutResponse;
    classAlias["org.atinject.api.authentication.dto.LoginResponse"] = LoginResponse;
    classAlias["org.atinject.core.latency.dto.PingResponse"] = PingResponse;
    classAlias["org.atinject.core.latency.dto.PingRequest"] = PingRequest;
    classAlias["org.atinject.api.registration.dto.RegisterAsGuestRequest"] = RegisterAsGuestRequest;
    classAlias["org.atinject.api.registration.dto.RegisterAsGuestResponse"] = RegisterAsGuestResponse;
    classAlias["org.atinject.api.registration.dto.RegisterRequest"] = RegisterRequest;
    classAlias["org.atinject.api.registration.dto.IsUsernameAvailableRequest"] = IsUsernameAvailableRequest;
    classAlias["org.atinject.api.registration.dto.IsUsernameAvailableResponse"] = IsUsernameAvailableResponse;
    classAlias["org.atinject.api.registration.dto.RegisterResponse"] = RegisterResponse;
    classAlias["org.atinject.core.rendezvous.dto.SessionJoinedRendezvousNotification"] = SessionJoinedRendezvousNotification;
    classAlias["org.atinject.core.rendezvous.dto.SessionLeftRendezvousNotification"] = SessionLeftRendezvousNotification;
    classAlias["org.atinject.core.rendezvous.dto.JoinRendezvousRequest"] = JoinRendezvousRequest;
    classAlias["org.atinject.core.rendezvous.dto.LeaveRendezvousRequest"] = LeaveRendezvousRequest;
    classAlias["org.atinject.core.session.dto.SessionOpenedNotification"] = SessionOpenedNotification;
    classAlias["org.atinject.api.systemproperty.dto.SystemProperty"] = SystemProperty;
    classAlias["org.atinject.core.topology.dto.GetTopologyResponse"] = GetTopologyResponse;
    classAlias["org.atinject.core.topology.dto.GetTopologyRequest"] = GetTopologyRequest;
    classAlias["org.atinject.api.user.dto.UpdateUserNameResponse"] = UpdateUserNameResponse;
    classAlias["org.atinject.api.user.dto.UpdateUserNameRequest"] = UpdateUserNameRequest;
    classAlias["org.atinject.api.user.dto.GetUserRequest"] = GetUserRequest;
    classAlias["org.atinject.api.user.dto.GetUserResponse"] = GetUserResponse;
    classAlias["org.atinject.api.user.dto.User"] = User;
    classAlias["org.atinject.api.useraffinity.dto.UserAffinityNotification"] = UserAffinityNotification;

    return classAlias;
});
