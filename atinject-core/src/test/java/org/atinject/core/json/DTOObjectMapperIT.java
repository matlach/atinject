package org.atinject.core.json;

import java.util.UUID;

import javax.inject.Inject;

import org.atinject.core.dto.DTO;
import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.core.session.dto.SessionOpenedNotification;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class DTOObjectMapperIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(DTOObjectMapperIT.class);
    }
    
    @Inject
    private Logger logger;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Test
    public void serializeSessionOpenedNotification(){
        
        SessionOpenedNotification newNotification = new SessionOpenedNotification();
        newNotification.setSessionId("123");
        
        String json = dtoObjectMapper.writeValueAsString(newNotification);
        
        SessionOpenedNotification notification = dtoObjectMapper.readValue(json);
        
        Assert.assertEquals(newNotification.getSessionId(), notification.getSessionId());
        
        logger.info(json);
    }
    
    
    private static class DTOWithUUID extends DTO {
        private static final long serialVersionUID = 1L;
        public UUID uuid;
    }
    
    @Test
    public void serializeDeserializeRandomUUID() {
        DTOWithUUID newDTOWithUUID = new DTOWithUUID();
        newDTOWithUUID.uuid = UUID.randomUUID();
        String json = dtoObjectMapper.writeValueAsString(newDTOWithUUID);
        DTOWithUUID unserialized = dtoObjectMapper.readValue(json);
        
        logger.info(json);
    }
}
