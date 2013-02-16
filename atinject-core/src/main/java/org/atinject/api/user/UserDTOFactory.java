package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.dto.GetUserResponse;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.dto.UserAffinityNotification;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.dto.DTOFactory;

@ApplicationScoped
public class UserDTOFactory extends DTOFactory
{

    public GetUserRequest newGetUserRequest(){
        return BeanManagerExtension.getReference(GetUserRequest.class);
    }
    
    public GetUserResponse newGetUserResponse(){
        return BeanManagerExtension.getReference(GetUserResponse.class);
    }
    
    public User newUser(){
        return BeanManagerExtension.getReference(User.class);
    }
    
    public UserAffinityNotification newUserAffinityNotification(){
        return BeanManagerExtension.getReference(UserAffinityNotification.class);
    }
}
