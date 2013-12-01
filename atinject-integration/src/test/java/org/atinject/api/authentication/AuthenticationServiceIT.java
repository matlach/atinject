package org.atinject.api.authentication;

import java.io.File;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.atinject.api.registration.GuestUsernamePasswordGenerator;
import org.atinject.api.registration.PasswordDigester;
import org.atinject.api.registration.RegistrationService;
import org.atinject.api.usersession.UserSession;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class AuthenticationServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File beansXmlFile = Paths.get("src/test/resources/default").resolve("beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive archive = createArchive(ArquillianIT.class);
        addPackageAndItIsDependencies(archive, api_authentication);
        
        archive
                //.addAsManifestResource(beansXmlFile, "beans.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject RegistrationService registrationService;
    @Inject GuestUsernamePasswordGenerator guestUsernamePasswordGenerator;
    @Inject AuthenticationService authenticationService;
    @Inject PasswordDigester passwordDigester;
    
    @Test
    public void testAuthentication() {
        String username = guestUsernamePasswordGenerator.generateUsername();
        String password = guestUsernamePasswordGenerator.generatePassword();
        
        registrationService.registerAsGuest(username, password);
        
        UserSession session = new UserSession()
            .setMachineId("")
            .setSessionId("")
            .setUserId(null);
        
        password = passwordDigester.digest(password);
        
        authenticationService.login(session, username, password);
    }
}
