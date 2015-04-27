package org.atinject.api.registration;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RegistrationServiceIT extends IntegrationTest {

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
    private RegistrationService registrationService;
    
    @Test @InSequence(1)
    public void testRegisterAsGuest() {
    	RegistratedUser registratedUser = registrationService.registerAsGuest();
    	Assertions.assertThat(registratedUser).isNotNull();
    }
    
    @Test @InSequence(2)
    public void testRegisterAsGuestWithGivenUsernameAndPassword() {
    	registrationService.registerAsGuest("username", "password");
    }
    
    @Test @InSequence(3)
    public void testRegisterFromGuest() {
    	registrationService.registerFromGuest(
    			"username", "password",
    			"username2", "password2");
    }
    
    @Test @InSequence(4)
    public void testRegisterWithNonAvailableUsername() {
    	registrationService.registerAsGuest("username2", "password2");
    }
}
