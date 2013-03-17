package org.atinject.api.authentication;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.authentication.dto.LoginRequest;
import org.atinject.api.authentication.dto.LoginResponse;
import org.atinject.api.authentication.dto.LogoutRequest;
import org.atinject.api.authentication.dto.LogoutResponse;
import org.atinject.core.cdi.CDI;
import org.atinject.core.dto.DTOFactory;

@ApplicationScoped
public class AuthenticationDTOFactory extends DTOFactory {

    public LoginRequest newLoginRequest(){
        return CDI.select(LoginRequest.class).get();
    }
    
    public LoginResponse newLoginResponse(){
        return CDI.select(LoginResponse.class).get();
    }
    
    public LogoutRequest newLogoutRequest(){
        return CDI.select(LogoutRequest.class).get();
    }
    
    public LogoutResponse newLogoutResponse(){
        return CDI.select(LogoutResponse.class).get();
    }
}
