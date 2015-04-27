package org.atinject.core.json;

import java.util.UUID;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.core.session.dto.SessionOpenedNotification;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.slf4j.Logger;

public class DTOObjectMapperIT extends IntegrationTest {

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
    private Logger logger;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Test @InSequence(1)
    public void serializeSessionOpenedNotification(){
        
        SessionOpenedNotification newNotification = new SessionOpenedNotification();
        newNotification.setSessionId("123");
        
        String json = dtoObjectMapper.writeValueAsString(newNotification);
        
        SessionOpenedNotification notification = dtoObjectMapper.readValue(json);
        
        Assertions.assertThat(newNotification.getSessionId()).isEqualTo(notification.getSessionId());
        
        logger.info(json);
    }
    
    
    @Test @InSequence(2)
    public void serializeDeserializeRandomUUID() {
        DummyDTO newDTOWithUUID = new DummyDTO();
        newDTOWithUUID.uuid = UUID.randomUUID();
        String json = dtoObjectMapper.writeValueAsString(newDTOWithUUID);
        DummyDTO unserialized = dtoObjectMapper.readValue(json);
        
        logger.info(json);
    }
}
