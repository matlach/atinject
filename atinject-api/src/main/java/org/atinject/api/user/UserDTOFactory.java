package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.useraffinity.dto.UserAffinityNotification;
import org.atinject.core.cdi.CDI;
import org.atinject.core.dto.DTOFactory;

@ApplicationScoped
public class UserDTOFactory extends DTOFactory
{

    public GetUserRequest newGetUserRequest(){
        return CDI.select(GetUserRequest.class).get();
    }
    
    public GetUserResponse newGetUserResponse(){
        return CDI.select(GetUserResponse.class).get();
    }
    
    public User newUser(){
        return CDI.select(User.class).get();
    }
    
    public UserAffinityNotification newUserAffinityNotification(){
        return CDI.select(UserAffinityNotification.class).get();
    }
}
