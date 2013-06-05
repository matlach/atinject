package org.atinject.core.json;

import javax.inject.Inject;

import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.core.session.dto.SessionOpenedNotification;
import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DTOObjectMapperIT extends IntegrationTest
{

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
    
}
