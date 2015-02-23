package org.atinject.integration;


import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ArquillianIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
    		.appendEmptyBeansXml()
    		.appendEmptyValidationXml()
    		.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
    		.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
    		.appendResource("arquillian-logback.xml", "logback.xml")
    		.appendResource("arquillian-jgroups.xml", "jgroups.xml")
    		.getArchive();
    }
    
    @Inject
    private UserTransaction ut;
    
    @Test @InSequence(1)
    public void testArquillianBootstrap() {
    	Assertions.assertThat(ut).isNotNull();
    }
    
}
