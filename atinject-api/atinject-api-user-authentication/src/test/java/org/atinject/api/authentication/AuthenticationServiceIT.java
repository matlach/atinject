package org.atinject.api.authentication;

import javax.inject.Inject;

import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.api.usersession.UserSession;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class AuthenticationServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
			.appendEmptyBeansXml()
			.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
			.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
			.appendResource("arquillian-logback.xml", "logback.xml")
			.appendResource("arquillian-jgroups.xml", "jgroups.xml")
			.getArchive();
    }
    
    @Inject
    private UserService userService;
    
    @Inject
    private UserCredentialService userCredentialService;
    
    @Inject
    private AuthenticationService authenticationService;
    
    @Test
    @InSequence(1)
    public void setup() {
    	UserEntity user = userService.addUser("test-user");
    	userCredentialService.addUserCredential(user.getId(), "test-user", "123");
    }
    
    @Test
    @InSequence(2)
    public void testAuthentication() {
        String username = "test-user";
        String password = "123";
        
        UserSession session = new UserSession()
            .setMachineId("")
            .setSessionId("")
            .setUserId(null);
        
        authenticationService.login(session, username, password);
    }
}
