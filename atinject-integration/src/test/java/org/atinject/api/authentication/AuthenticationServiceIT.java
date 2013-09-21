package org.atinject.api.authentication;

import javax.inject.Inject;

import org.atinject.api.registration.RegistrationService;
import org.atinject.api.usersession.UserSession;
import org.atinject.integration.IntegrationTest;
import org.jgroups.util.UUID;
import org.junit.Test;

public class AuthenticationServiceIT extends IntegrationTest
{

    @Inject RegistrationService registrationService;
    @Inject AuthenticationService authenticationService;
    
    @Test
    public void testAuthentication(){
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        
        registrationService.registerAsGuest(username, password);
        
        UserSession session = new UserSession()
            .setMachineId("")
            .setSessionId("")
            .setUserId(null);
        
        authenticationService.login(session, username, password);
    }
}
