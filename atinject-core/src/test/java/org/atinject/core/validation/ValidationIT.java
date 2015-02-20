package org.atinject.core.validation;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ValidationIT extends IntegrationTest {
	
    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(ValidationIT.class);
    }
    
    @Inject
    private Validator validator;
    
    @Test @InSequence(1)
    public void testValidation() {
        validator.validate(new String());
    }
    
}
