package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.authentication.dto.LoginRequest;
import org.atinject.api.authentication.dto.LoginResponse;
import org.atinject.api.authentication.dto.LogoutRequest;
import org.atinject.api.authentication.dto.LogoutResponse;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.dto.DTOFactory;

@ApplicationScoped
public class AuthenticationDTOFactory extends DTOFactory {

    public LoginRequest newLoginRequest(){
        return BeanManagerExtension.getReference(LoginRequest.class);
    }
    
    public LoginResponse newLoginResponse(){
        return BeanManagerExtension.getReference(LoginResponse.class);
    }
    
    public LogoutRequest newLogoutRequest(){
        return BeanManagerExtension.getReference(LogoutRequest.class);
    }
    
    public LogoutResponse newLogoutResponse(){
        return BeanManagerExtension.getReference(LogoutResponse.class);
    }
}
