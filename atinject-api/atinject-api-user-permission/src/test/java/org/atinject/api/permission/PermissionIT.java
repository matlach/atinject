package org.atinject.api.permission;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class PermissionIT extends IntegrationTest {

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
    
    @Inject PermissionService permissionService;
    
    @Test
    public void testDummyPermission(){
    	Assertions.assertThat(true).isTrue();
    }
}
