package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.useraffinity.dto.UserAffinityNotification;
import org.atinject.core.dto.DTOFactory;

@ApplicationScoped
public class UserDTOFactory extends DTOFactory {

    public GetUserRequest newGetUserRequest() {
        return new GetUserRequest();
    }
    
    public GetUserResponse newGetUserResponse() {
        return new GetUserResponse();
    }
    
    public User newUser() {
        return new User();
    }
    
    public UserAffinityNotification newUserAffinityNotification() {
        return new UserAffinityNotification();
    }
}
